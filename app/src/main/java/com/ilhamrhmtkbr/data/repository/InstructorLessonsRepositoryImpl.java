package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.instructor.LessonMapper;
import com.ilhamrhmtkbr.domain.model.common.Lesson;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.repository.InstructorLessonsRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorLessonUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorLessonsResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorLessonsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorLessonsPagination;
import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorLessonsRepositoryImpl implements InstructorLessonsRepository {
    private final InstructorApi api;
    private final InstructorLessonsDao lessonsDao;
    private final InstructorLessonsPagination lessonsPagination;
    private final MediatorLiveData<Resource<List<Lesson>>> lessonsResult = new MediatorLiveData<>();

    @Inject
    public InstructorLessonsRepositoryImpl(InstructorApi api, InstructorLessonsDao lessonsDao, InstructorLessonsPagination lessonsPagination) {
        this.api = api;
        this.lessonsDao = lessonsDao;
        this.lessonsPagination = lessonsPagination;
    }

    @Override
    public void fetch(String page, String sectionId) {
        lessonsResult.setValue(Resource.loading());
        api.lessonsFetch(page, sectionId).enqueue(new Callback<BaseResponseApi<InstructorLessonsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<InstructorLessonsResponse>> call, Response<BaseResponseApi<InstructorLessonsResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            lessonsDao.refresh(LessonMapper.fromResponseToListEntities(response.body().data));
                            lessonsPagination.refresh(LessonMapper.fromResponseToPaginationListEntities(response.body().data));
                        }
                    });
                    lessonsResult.setValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        lessonsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<InstructorLessonsResponse>> call, Throwable t) {
                lessonsResult.setValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Lesson>>> getLessons(String sectionId) {
        LiveData<List<InstructorLessonsEntity>> dbSource = lessonsDao.getAll();
        lessonsResult.addSource(dbSource, new Observer<List<InstructorLessonsEntity>>() {
            @Override
            public void onChanged(List<InstructorLessonsEntity> instructorLessonsEntities) {
                if (instructorLessonsEntities.isEmpty()) {
                    fetch("1", sectionId);
                } else {
                    List<Lesson> lessons = LessonMapper.fromEntitiesToFromList(instructorLessonsEntities);
                    if (lessonsResult.getValue() == null || !lessonsResult.getValue().isLoading()) {
                        lessonsResult.setValue(Resource.success(lessons));
                    }
                }
            }
        });

        return lessonsResult;
    }

    @Override
    public LiveData<List<Page>> getLessonsPagination() {
        return Transformations.map(lessonsPagination.getAll(), new Function1<List<InstructorLessonsPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<InstructorLessonsPaginationEntity> instructorLessonsPaginationEntities) {
                List<Page> pages = new ArrayList<>();

                for (InstructorLessonsPaginationEntity item : instructorLessonsPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }
                return pages;
            }
        });
    }

    @Override
    public void store(InstructorLessonUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.lessonStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() !=null){
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
    public void modify(InstructorLessonUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.lessonModify(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() !=null){
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
    public void delete(String sectionId, String lessonId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.lessonDelete(sectionId, lessonId).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() !=null){
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
}
