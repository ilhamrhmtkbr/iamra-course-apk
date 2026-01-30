package com.ilhamrhmtkbr.presentation.instructor.course;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCourseUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCourseResponse;
import com.ilhamrhmtkbr.domain.repository.InstructorCoursesRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CourseViewModel extends ViewModel implements ValidationFrontend<InstructorCourseUpSertRequest> {
    private final InstructorCoursesRepository coursesRepository;
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();
    private final SingleLiveEvent<FormState<String>> courseFormState = new SingleLiveEvent<>();

    @Inject
    public CourseViewModel(InstructorCoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    public void fetch(String courseId) {
        coursesRepository.show(courseId);
    }

    public LiveData<Resource<InstructorCourseResponse>> getCourse() {
        return coursesRepository.getCourseResult();
    }

    public void store(String title, String description, String image, String price, String level, String status, String visibility, String notes, String requirements, String editor) {
        InstructorCourseUpSertRequest request = new InstructorCourseUpSertRequest();
        request.title = title;
        request.description = description;
        request.image = image;
        request.price = price;
        request.level = level;
        request.status = status;
        request.visibility = visibility;
        request.notes = notes;
        request.requirements = requirements;
        request.editor = editor;

        if (isValidValue(request)) {
            coursesRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    courseFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String id, String title, String description, String image, String price, String level, String status, String visibility, String notes, String requirements, String editor) {
        InstructorCourseUpSertRequest request = new InstructorCourseUpSertRequest();
        request.id = id;
        request.title = title;
        request.description = description;
        request.image = image;
        request.price = price;
        request.level = level;
        request.status = status;
        request.visibility = visibility;
        request.notes = notes;
        request.requirements = requirements;
        request.editor = editor;

        if (isValidValue(request)) {
            coursesRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    courseFormState.postValue(state);
                }
            });
        }
    }

    public void delete(String courseId) {
        coursesRepository.delete(courseId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                courseFormState.postValue(state);
            }
        });
    }

    @Override
    public boolean isValidValue(InstructorCourseUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();
        BaseValidation title = new BaseValidation().validation("title", request.title).required();
        BaseValidation description = new BaseValidation().validation("description", request.description).required();
        BaseValidation image = new BaseValidation().validation("image", request.image).required();
        BaseValidation price = new BaseValidation().validation("price", request.price).required();
        BaseValidation level = new BaseValidation().validation("level", request.level).required();
        BaseValidation status = new BaseValidation().validation("status", request.status).required();
        BaseValidation visibility = new BaseValidation().validation("visibility", request.visibility).required();
        BaseValidation notes = new BaseValidation().validation("notes", request.notes).required();
        BaseValidation requirements = new BaseValidation().validation("requirements", request.requirements).required();
        BaseValidation editor = new BaseValidation().validation("editor", request.editor).required();

        if (title.hasError()) {
            errors.put(title.getFieldName(), title.getError());
        }
        if (description.hasError()) {
            errors.put(description.getFieldName(), description.getError());
        }
        if (image.hasError()) {
            errors.put(image.getFieldName(), image.getError());
        }
        if (price.hasError()) {
            errors.put(price.getFieldName(), price.getError());
        }
        if (level.hasError()) {
            errors.put(level.getFieldName(), level.getError());
        }
        if (status.hasError()) {
            errors.put(status.getFieldName(), status.getError());
        }
        if (visibility.hasError()) {
            errors.put(visibility.getFieldName(), visibility.getError());
        }
        if (notes.hasError()) {
            errors.put(notes.getFieldName(), notes.getError());
        }
        if (requirements.hasError()) {
            errors.put(requirements.getFieldName(), requirements.getError());
        }
        if (editor.hasError()) {
            errors.put(editor.getFieldName(), editor.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public MutableLiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    public SingleLiveEvent<FormState<String>> getCourseFormState() {
        return courseFormState;
    }

    public void refreshCourses() {
        coursesRepository.fetch("1", "desc");
    }
}
