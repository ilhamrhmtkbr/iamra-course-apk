package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.student.QuestionMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Question;
import com.ilhamrhmtkbr.domain.repository.StudentQuestionRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseQuestionUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentQuestionResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentQuestionsResponse;
import com.ilhamrhmtkbr.data.local.dao.StudentQuestionsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentQuestionsPagination;
import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsPaginationEntity;
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

public class StudentQuestionRepositoryImpl implements StudentQuestionRepository {
    private final MediatorLiveData<Resource<List<Question>>> questionsResult = new MediatorLiveData<>();
    private final StudentApi api;
    private final StudentQuestionsDao studentQuestionsDao;
    private final StudentQuestionsPagination studentQuestionsPagination;
    private final SingleLiveEvent<Resource<Question>> studentQuestionDetailResult = new SingleLiveEvent<>();

    @Inject
    public StudentQuestionRepositoryImpl(StudentApi api, StudentQuestionsDao studentQuestionsDao, StudentQuestionsPagination studentQuestionsPagination) {
        this.api = api;
        this.studentQuestionsDao = studentQuestionsDao;
        this.studentQuestionsPagination = studentQuestionsPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        questionsResult.postValue(Resource.loading());
        api.questionsFetch(page, sort).enqueue(new Callback<BaseResponseApi<StudentQuestionsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentQuestionsResponse>> call, Response<BaseResponseApi<StudentQuestionsResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            studentQuestionsDao.refresh(QuestionMapper.fromResponseToEntities(response.body().data));
                            studentQuestionsPagination.refreshAll(QuestionMapper.fromResponseToPaginationEntities(response.body().data));
                        }
                    });
                    questionsResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        questionsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentQuestionsResponse>> call, Throwable t) {
                questionsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Question>>> getAllQuestions() {
        LiveData<List<StudentQuestionsEntity>> dbSource = studentQuestionsDao.getAll();
        questionsResult.addSource(dbSource, new Observer<List<StudentQuestionsEntity>>() {
            @Override
            public void onChanged(List<StudentQuestionsEntity> studentQuestionsEntities) {
                if (studentQuestionsEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Question> questions = QuestionMapper.fromEntitiesToList(studentQuestionsEntities);
                    if (questionsResult.getValue() == null || !questionsResult.getValue().isLoading()) {
                        questionsResult.setValue(Resource.success(questions));
                    }
                }
            }
        });

        return questionsResult;
    }

    @Override
    public LiveData<List<Page>> getPaginationData() {
        return Transformations.map(studentQuestionsPagination.getAll(), new Function1<List<StudentQuestionsPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<StudentQuestionsPaginationEntity> studentQuestionsPaginationEntities) {
                List<Page> pages = new ArrayList<>();
                for (StudentQuestionsPaginationEntity item : studentQuestionsPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }
                return pages;
            }
        });
    }

    @Override
    public void show(String questionId) {
        studentQuestionDetailResult.postValue(Resource.loading());
        api.questionFetch(questionId).enqueue(new Callback<BaseResponseApi<List<StudentQuestionResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<List<StudentQuestionResponse>>> call, Response<BaseResponseApi<List<StudentQuestionResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    studentQuestionDetailResult.postValue(Resource.success(QuestionMapper.fromResponseToQuestionDetail(response.body().data.get(0))));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<List<StudentQuestionResponse>>> call, Throwable t) {
                studentQuestionDetailResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<Question>> getStudentQuestionDetailResult() {
        return studentQuestionDetailResult;
    }

    @Override
    public void store(StudentCourseQuestionUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.questionStore(request).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null){
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<String>();
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
    public void modify(StudentCourseQuestionUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.questionModify(request).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null){
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<String>();
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
    public void delete(String questionId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.questionDelete(questionId).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<String>();
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
