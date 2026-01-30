package com.ilhamrhmtkbr.presentation.student.question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseQuestionUpSertRequest;
import com.ilhamrhmtkbr.domain.model.student.Question;
import com.ilhamrhmtkbr.domain.repository.StudentQuestionRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class QuestionViewModel extends ViewModel implements ValidationFrontend<StudentCourseQuestionUpSertRequest> {
    private final StudentQuestionRepository questionRepository;
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();
    private final SingleLiveEvent<FormState<String>> questionFormState = new SingleLiveEvent<>();

    @Inject
    public QuestionViewModel(StudentQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void show(String questionId) {
        questionRepository.show(questionId);
    }

    public LiveData<Resource<Question>> getQuestion() {
        return questionRepository.getStudentQuestionDetailResult();
    }

    public void store(String question, String courseId) {
        StudentCourseQuestionUpSertRequest request = new StudentCourseQuestionUpSertRequest();
        request.question = question;
        request.instructorCourseId = courseId;

        if (isValidValue(request)) {
            questionRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    questionFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String questionId, String courseId, String question) {
        StudentCourseQuestionUpSertRequest request = new StudentCourseQuestionUpSertRequest();
        request.id = questionId;
        request.instructorCourseId = courseId;
        request.question = question;

        if (isValidValue(request)) {
            questionRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    questionFormState.postValue(state);
                }
            });
        }
    }

    public void delete(String questionId) {
        questionRepository.delete(questionId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                questionFormState.postValue(state);
            }
        });
    }

    public void refreshQuestions(){
        questionRepository.fetch("1", "desc");
    }

    @Override
    public boolean isValidValue(StudentCourseQuestionUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation question = new BaseValidation().validation("question", request.question).required();

        if (question.hasError()){
            errors.put(question.getFieldName(), question.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public MutableLiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    public SingleLiveEvent<FormState<String>> getQuestionFormState() {
        return questionFormState;
    }
}
