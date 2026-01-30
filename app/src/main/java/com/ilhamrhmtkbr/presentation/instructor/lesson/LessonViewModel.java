package com.ilhamrhmtkbr.presentation.instructor.lesson;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorLessonUpSertRequest;
import com.ilhamrhmtkbr.domain.repository.InstructorLessonsRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LessonViewModel extends ViewModel implements ValidationFrontend<InstructorLessonUpSertRequest> {
    private final InstructorLessonsRepository lessonsRepository;
    private final SingleLiveEvent<FormState<String>> lessonFormState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();

    @Inject
    public LessonViewModel(InstructorLessonsRepository lessonsRepository) {
        this.lessonsRepository = lessonsRepository;
    }

    public void store(String instructorSectionId, String title, String description, String code, String orderInSection) {
        InstructorLessonUpSertRequest request = new InstructorLessonUpSertRequest();
        request.instructorSectionId = instructorSectionId;
        request.title = title;
        request.description = description;
        request.code = code;
        request.orderInSection = orderInSection;
        if (isValidValue(request)) {
            lessonsRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    lessonFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String id, String instructorSectionId, String title, String description, String code, String orderInSection) {
        InstructorLessonUpSertRequest request = new InstructorLessonUpSertRequest();
        request.id = id;
        request.instructorSectionId = instructorSectionId;
        request.title = title;
        request.description = description;
        request.code = code;
        request.orderInSection = orderInSection;
        if (isValidValue(request)) {
            lessonsRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    lessonFormState.postValue(state);
                }
            });
        }
    }

    public void delete(String sectionId,String lessonId) {
        lessonsRepository.delete(sectionId, lessonId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                lessonFormState.postValue(state);
            }
        });
    }

    @Override
    public boolean isValidValue(InstructorLessonUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation title = new BaseValidation().validation("title", request.title).required();
        BaseValidation description = new BaseValidation().validation("description", request.description).required();
        BaseValidation code = new BaseValidation().validation("code", request.code).required();
        BaseValidation orderInSection = new BaseValidation().validation("order_in_section", request.orderInSection).required();

        if (title.hasError()) {
            errors.put(title.getFieldName(), title.getError());
        }
        if (description.hasError()) {
            errors.put(description.getFieldName(), description.getError());
        }
        if (code.hasError()) {
            errors.put(code.getFieldName(), code.getError());
        }
        if (orderInSection.hasError()) {
            errors.put(orderInSection.getFieldName(), orderInSection.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public SingleLiveEvent<FormState<String>> getLessonFormState() {
        return lessonFormState;
    }

    public MutableLiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    public InstructorLessonsRepository getLessonsRepository() {
        return lessonsRepository;
    }
}