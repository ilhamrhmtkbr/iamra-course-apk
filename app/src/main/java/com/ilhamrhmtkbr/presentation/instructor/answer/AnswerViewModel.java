package com.ilhamrhmtkbr.presentation.instructor.answer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAnswerUpSertRequest;
import com.ilhamrhmtkbr.domain.repository.InstructorAnswersRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AnswerViewModel extends ViewModel implements ValidationFrontend<InstructorAnswerUpSertRequest> {
    private final InstructorAnswersRepository answersRepository;
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();
    private final SingleLiveEvent<FormState<String>> answerFormState = new SingleLiveEvent<>();

    @Inject
    public AnswerViewModel(InstructorAnswersRepository answersRepository) {
        this.answersRepository = answersRepository;
    }

    public void refreshAnswers(){
        answersRepository.fetch("1", "desc");
    }

    public void store(String answer, String questionId) {
        InstructorAnswerUpSertRequest request = new InstructorAnswerUpSertRequest();
        request.answer = answer;
        request.studentQuestionId = questionId;
        if (isValidValue(request)) {
            answersRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    answerFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String answer, String answerId) {
        InstructorAnswerUpSertRequest request = new InstructorAnswerUpSertRequest();
        request.answer = answer;
        request.id = answerId;
        if (isValidValue(request)) {
            answersRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    answerFormState.postValue(state);
                }
            });
        }
    }

    @Override
    public boolean isValidValue(InstructorAnswerUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation answer = new BaseValidation().validation("answer", request.answer)
                .required().minMax(1, 100);

        if (answer.hasError()) {
            errors.put(answer.getFieldName(), answer.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    public SingleLiveEvent<FormState<String>> getAnswerFormState() {
        return answerFormState;
    }
}
