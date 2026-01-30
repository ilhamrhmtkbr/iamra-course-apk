package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.student.StudiesMapper;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.repository.StudentStudiesRepository;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentLessonsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentSectionsResponse;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCoursesResponse;
import com.ilhamrhmtkbr.data.local.dao.StudentCoursesDao;
import com.ilhamrhmtkbr.data.local.dao.StudentCoursesPagination;
import com.ilhamrhmtkbr.data.local.entity.StudentCoursesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCoursesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.StudentApi;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentStudiesRepositoryImpl implements StudentStudiesRepository {
    private final MediatorLiveData<Resource<List<Course>>> coursesResult = new MediatorLiveData<>();
    private final StudentApi api;
    private final StudentCoursesDao studentCoursesDao;
    private final StudentCoursesPagination studentCoursesPagination;
    private final SingleLiveEvent<Resource<List<StudentSectionsResponse>>> sectionsResult = new SingleLiveEvent<>();
    private final SingleLiveEvent<Resource<StudentLessonsResponse>> lessonsResult = new SingleLiveEvent<>();

    @Inject
    public StudentStudiesRepositoryImpl(StudentApi api, StudentCoursesDao studentCoursesDao, StudentCoursesPagination studentCoursesPagination) {
        this.api = api;
        this.studentCoursesDao = studentCoursesDao;
        this.studentCoursesPagination = studentCoursesPagination;
    }

    @Override
    public void courses(String page, String sort) {
        coursesResult.postValue(Resource.loading());
        api.coursesFetch(page, sort).enqueue(new Callback<BaseResponseApi<StudentCoursesResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentCoursesResponse>> call, Response<BaseResponseApi<StudentCoursesResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            studentCoursesDao.refresh(StudiesMapper.fromResponseToEntities(response.body().data));
                            studentCoursesPagination.refresh(StudiesMapper.fromResponseToPaginationEntities(response.body().data));
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
            public void onFailure(Call<BaseResponseApi<StudentCoursesResponse>> call, Throwable t) {
                coursesResult.setValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<List<Page>> getPaginationData() {
        return Transformations.map(studentCoursesPagination.getAll(), new Function1<List<StudentCoursesPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<StudentCoursesPaginationEntity> studentCoursesPaginationEntities) {
                List<Page> pages = new ArrayList<>();
                for (StudentCoursesPaginationEntity item : studentCoursesPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }
                return pages;
            }
        });
    }

    @Override
    public LiveData<Resource<List<Course>>> getAllCourses() {
        LiveData<List<StudentCoursesEntity>> dbSource = studentCoursesDao.getAll();
        coursesResult.addSource(dbSource, new Observer<List<StudentCoursesEntity>>() {
            @Override
            public void onChanged(List<StudentCoursesEntity> studentCoursesEntities) {
                if (studentCoursesEntities.isEmpty()) {
                    courses("1", "desc");
                } else {
                    List<Course> courses = StudiesMapper.fromEntitiesToList(studentCoursesEntities);
                    if (coursesResult.getValue() == null || !coursesResult.getValue().isLoading()) {
                        coursesResult.setValue(Resource.success(courses));
                    }
                }
            }
        });
        return coursesResult;
    }

    @Override
    public void sections(String courseId) {
        sectionsResult.postValue(Resource.loading());
        api.sectionsFetch(courseId).enqueue(new Callback<BaseResponseApi<List<StudentSectionsResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<List<StudentSectionsResponse>>> call, Response<BaseResponseApi<List<StudentSectionsResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    sectionsResult.postValue(Resource.success(response.body().data));
                } else {
                    try {
                        sectionsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<List<StudentSectionsResponse>>> call, Throwable t) {
                sectionsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<StudentSectionsResponse>>> getSectionsResult() {
        return sectionsResult;
    }

    @Override
    public void lessons(String sectionId, String page) {
        lessonsResult.postValue(Resource.loading());
        api.lessonsFetch(sectionId, page).enqueue(new Callback<BaseResponseApi<StudentLessonsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentLessonsResponse>> call, Response<BaseResponseApi<StudentLessonsResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    lessonsResult.postValue(Resource.success(response.body().data));
                } else {
                    try {
                        sectionsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentLessonsResponse>> call, Throwable t) {
                sectionsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<StudentLessonsResponse>> getLessonsResult() {
        return lessonsResult;
    }
}
