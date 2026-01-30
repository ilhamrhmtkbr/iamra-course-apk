package com.ilhamrhmtkbr.presentation.member.roleregister;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberRegisterAsStudentRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@HiltViewModel
public class RoleRegisterViewModel extends ViewModel {
    private final MemberRepository memberRepository;
    private final MutableLiveData<Map<String, String>> validationErrorsFrontendStudent = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> validationErrorsFrontendInstructor = new MutableLiveData<>();
    private final SingleLiveEvent<FormState<String>> studentFormState = new SingleLiveEvent<>();
    private final SingleLiveEvent<FormState<String>> instructorFormState = new SingleLiveEvent<>();

    @Inject
    public RoleRegisterViewModel(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void registerAsStudent(String category, String summary) {
        MemberRegisterAsStudentRequest request = new MemberRegisterAsStudentRequest("student", category, summary);
        if (isValidValueStudent(request)) {
            memberRepository.registerAsStudent(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    studentFormState.postValue(state);
                }
            });
        }
    }

    // Di dalam RoleRegisterViewModel.java

    public void registerAsInstructor(String roleValue, String summaryValue, File resumeFile) {
        if (isValidValueInstructor(roleValue, summaryValue)) {
            instructorFormState.postValue(FormState.loading());

            RequestBody roleBody = RequestBody.create(MediaType.parse("text/plain"), roleValue);
            RequestBody summaryBody = RequestBody.create(MediaType.parse("text/plain"), summaryValue);

            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), resumeFile);
            MultipartBody.Part resumePart = MultipartBody.Part.createFormData("resume", resumeFile.getName(), requestFile);

            memberRepository.registerAsInstructor(roleBody, summaryBody, resumePart, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    instructorFormState.postValue(state);
                }
            });
        }
    }

    private boolean isValidValueStudent(MemberRegisterAsStudentRequest request) {
        Map<String, String> errors = new HashMap<>();

        String[] categories = {"learner", "employee", "jobless"};
        BaseValidation category = new BaseValidation().validation("category", request.category).required();
        BaseValidation summary = new BaseValidation().validation("summary", request.summary).required().minMax(10, 125);

        if (category.hasError()) {
            errors.put(category.getFieldName(), category.getError());
        }

        if (summary.hasError()) {
            errors.put(summary.getFieldName(), summary.getError());
        }

        validationErrorsFrontendStudent.setValue(errors);

        return errors.isEmpty();
    }

    private boolean isValidValueInstructor(String roleValue, String summaryValue) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation role = new BaseValidation().validation("role", roleValue).required();
        BaseValidation summary = new BaseValidation().validation("summary", summaryValue).required().minMax(10, 125);

        if (role.hasError()) {
            errors.put(role.getFieldName(), role.getError());
        }

        if (summary.hasError()) {
            errors.put(summary.getFieldName(), summary.getError());
        }

        validationErrorsFrontendInstructor.setValue(errors);

        return errors.isEmpty();
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontendStudent() {
        return validationErrorsFrontendStudent;
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontendInstructor() {
        return validationErrorsFrontendInstructor;
    }

    public LiveData<FormState<String>> getStudentFormState() {
        return studentFormState;
    }

    public LiveData<FormState<String>> getInstructorFormState() {
        return instructorFormState;
    }

    public void refreshUserData() {
        memberRepository.getMe(new FormCallback<UserResponse>() {
            @Override
            public void onResult(FormState<UserResponse> state) {

            }
        });
    }
}
