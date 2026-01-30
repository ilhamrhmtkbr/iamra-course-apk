package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.instructor.AnswerMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Answer;
import com.ilhamrhmtkbr.domain.repository.InstructorAnswersRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAnswerUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorAnswersResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorAnswersDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorAnswersPagination;
import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersPaginationEntity;
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

public class InstructorAnswersRepositoryImpl implements InstructorAnswersRepository {
    private final InstructorApi api;
    private final InstructorAnswersDao answersDao;
    private final InstructorAnswersPagination answersPagination;
    private final MediatorLiveData<Resource<List<Answer>>> answersResult = new MediatorLiveData<>();

    @Inject
    public InstructorAnswersRepositoryImpl(InstructorApi api, InstructorAnswersDao answersDao, InstructorAnswersPagination answersPagination) {
        this.api = api;
        this.answersDao = answersDao;
        this.answersPagination = answersPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        answersResult.postValue(Resource.loading());
        api.answersFetch(page, sort).enqueue(new Callback<InstructorAnswersResponse>() {
            @Override
            public void onResponse(Call<InstructorAnswersResponse> call, Response<InstructorAnswersResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            answersDao.refresh(AnswerMapper.fromResponseToEntities(response.body()));
                            answersPagination.refresh(AnswerMapper.fromResponseToPaginationEntities(response.body()));
                        }
                    });
                    answersResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null) {
                    try {
                        answersResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstructorAnswersResponse> call, Throwable t) {
                answersResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Answer>>> getAnswers() {
        LiveData<List<InstructorAnswersEntity>> dbSource = answersDao.getAll();

        answersResult.addSource(dbSource, new Observer<List<InstructorAnswersEntity>>() {
            @Override
            public void onChanged(List<InstructorAnswersEntity> instructorAnswersEntities) {
                if (instructorAnswersEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Answer> answers = AnswerMapper.fromEntitiesToList(instructorAnswersEntities);

                    if (answersResult.getValue() == null || !answersResult.getValue().isLoading()) {
                        answersResult.setValue(Resource.success(answers));
                    }
                }
            }
        });

        return answersResult;
    }

    @Override
    public LiveData<List<Page>> getAnswersPagination() {
        return Transformations.map(answersPagination.getAll(), new Function1<List<InstructorAnswersPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<InstructorAnswersPaginationEntity> instructorAnswersPaginationEntities) {
                List<Page> pages = new ArrayList<>();
                for (InstructorAnswersPaginationEntity item : instructorAnswersPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }

                return pages;
            }
        });
    }

    @Override
    public void store(InstructorAnswerUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.answerStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
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
    public void modify(InstructorAnswerUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.answerModify(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful()) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
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
