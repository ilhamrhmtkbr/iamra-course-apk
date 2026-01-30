package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesLikesEntity;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.instructor.CourseMapper;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.CoursesLike;
import com.ilhamrhmtkbr.domain.repository.InstructorCoursesRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCourseUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCourseResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCoursesLikesResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCoursesResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorCoursesDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorCoursesLikesDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorCoursesPagination;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class InstructorCoursesRepositoryImpl implements InstructorCoursesRepository {
    private final InstructorApi api;
    private final InstructorCoursesDao coursesDao;
    private final InstructorCoursesPagination coursesPagination;
    private final InstructorCoursesLikesDao coursesLikesDao;
    private final MediatorLiveData<Resource<List<Course>>> coursesResult = new MediatorLiveData<>();
    private final SingleLiveEvent<Resource<InstructorCourseResponse>> courseResult = new SingleLiveEvent<>();
    private final MediatorLiveData<Resource<List<CoursesLike>>> courseLikesResult = new MediatorLiveData<>();

    @Inject
    public InstructorCoursesRepositoryImpl(
            InstructorApi api,
            InstructorCoursesDao coursesDao,
            InstructorCoursesPagination coursesPagination,
            InstructorCoursesLikesDao coursesLikesDao
    ) {
        this.api = api;
        this.coursesDao = coursesDao;
        this.coursesPagination = coursesPagination;
        this.coursesLikesDao = coursesLikesDao;
    }

    @Override
    public void fetch(String page, String sort) {
        coursesResult.postValue(Resource.loading());
        api.coursesFetch(page, sort).enqueue(new Callback<InstructorCoursesResponse>() {
            @Override
            public void onResponse(Call<InstructorCoursesResponse> call, Response<InstructorCoursesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            coursesDao.refresh(CourseMapper.fromResponseToEntities(response.body()));
                            coursesPagination.refresh(CourseMapper.fromResponseToPaginationEntities(response.body()));
                        }
                    });
                    coursesResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null) {
                    try {
                        coursesResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstructorCoursesResponse> call, Throwable t) {
                coursesResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Course>>> getCourses(String page, String sort) {
        LiveData<List<InstructorCoursesEntity>> dbSource = coursesDao.getAll();

        coursesResult.addSource(dbSource, new Observer<List<InstructorCoursesEntity>>() {
            @Override
            public void onChanged(List<InstructorCoursesEntity> instructorCoursesEntities) {
                if (instructorCoursesEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Course> courses = CourseMapper.fromEntitiesToList(instructorCoursesEntities);
                    if (coursesResult.getValue() == null || !coursesResult.getValue().isLoading()) {
                        coursesResult.setValue(Resource.success(courses));
                    }
                }
            }
        });

        return coursesResult;
    }

    @Override
    public LiveData<List<Page>> getCoursesPagination() {
        return Transformations.map(coursesPagination.getAll(), entities -> {
            List<Page> pages = new ArrayList<>();
            for (InstructorCoursesPaginationEntity item : entities) {
                pages.add(new Page(item.url, item.label, item.isActive));
            }
            return pages;
        });
    }

    @Override
    public void show(String courseId) {
        courseResult.postValue(Resource.loading());
        api.courseFetch(courseId).enqueue(new Callback<BaseResponseApi<InstructorCourseResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<InstructorCourseResponse>> call, Response<BaseResponseApi<InstructorCourseResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    courseResult.postValue(Resource.success(response.body().data));
                } else if (response.errorBody() != null) {
                    try {
                        courseResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<InstructorCourseResponse>> call, Throwable t) {
                coursesResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public SingleLiveEvent<Resource<InstructorCourseResponse>> getCourseResult() {
        return courseResult;
    }

    @Override
    public void store(InstructorCourseUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.courseStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<Void>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void modify(InstructorCourseUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.courseModify(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<Void>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void delete(String courseId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.courseDelete(courseId).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                }
                if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<Void>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void fetchCourseLikes() {
        courseLikesResult.postValue(Resource.loading());
        api.courseLikesFetch().enqueue(new Callback<BaseResponseApi<List<InstructorCoursesLikesResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<List<InstructorCoursesLikesResponse>>> call, Response<BaseResponseApi<List<InstructorCoursesLikesResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            coursesLikesDao.refresh(CourseMapper.fromResponseCourseLikesToEntities(response.body().data));
                        }
                    });
                    courseLikesResult.postValue(Resource.success(null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<List<InstructorCoursesLikesResponse>>> call, Throwable t) {
            }
        });
    }

    @Override
    public LiveData<Resource<List<CoursesLike>>> getCoursesLikes() {
        LiveData<List<InstructorCoursesLikesEntity>> dbSource = coursesLikesDao.getAll();

        courseLikesResult.addSource(dbSource, new Observer<List<InstructorCoursesLikesEntity>>() {
            @Override
            public void onChanged(List<InstructorCoursesLikesEntity> instructorCoursesLikesEntities) {
                if (instructorCoursesLikesEntities.isEmpty()) {
                    fetchCourseLikes();
                } else {
                    List<CoursesLike> coursesLikes = CourseMapper.fromEntitiesToCourseLikeModel(instructorCoursesLikesEntities);
                    if (courseLikesResult.getValue() == null || !courseLikesResult.getValue().isLoading()) {
                        courseLikesResult.setValue(Resource.success(coursesLikes));
                    }
                }
            }
        });
        return courseLikesResult;
    }
}
