package com.ilhamrhmtkbr.presentation.instructor.section;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSectionUpSertRequest;
import com.ilhamrhmtkbr.domain.repository.InstructorSectionsRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SectionViewModel extends ViewModel implements ValidationFrontend<InstructorSectionUpSertRequest> {
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();
    private final InstructorSectionsRepository sectionsRepository;
    private final SingleLiveEvent<FormState<String>> sectionFormState = new SingleLiveEvent<>();

    @Inject
    public SectionViewModel(InstructorSectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    public void store(String courseId, String title, String orderInCourse) {
        InstructorSectionUpSertRequest request = new InstructorSectionUpSertRequest();
        request.instructorCourseId = courseId;
        request.title = title;
        request.orderInCourse = orderInCourse;
        if (isValidValue(request)) {
            sectionsRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    sectionFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String sectionId, String courseId, String title, String orderInCourse) {
        InstructorSectionUpSertRequest request = new InstructorSectionUpSertRequest();
        request.id = sectionId;
        request.instructorCourseId = courseId;
        request.title = title;
        request.orderInCourse = orderInCourse;
        if (isValidValue(request)) {
            sectionsRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    sectionFormState.postValue(state);
                }
            });
        }
    }

    public void delete(String sectionId) {
        sectionsRepository.delete(sectionId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                sectionFormState.postValue(state);
            }
        });
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    @Override
    public boolean isValidValue(InstructorSectionUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation title = new BaseValidation().validation("title", request.title).required();
        BaseValidation orderInCourse = new BaseValidation().validation("order_in_course", request.orderInCourse).required();

        if (title.hasError()) {
            errors.put(title.getFieldName(), title.getError());
        }
        if (orderInCourse.hasError()) {
            errors.put(orderInCourse.getFieldName(), orderInCourse.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public SingleLiveEvent<FormState<String>> getSectionFormState() {
        return sectionFormState;
    }

    public InstructorSectionsRepository getSectionsRepository() {
        return sectionsRepository;
    }
}