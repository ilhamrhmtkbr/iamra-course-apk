package com.ilhamrhmtkbr.presentation.guest.coursedetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCartStoreRequest;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.domain.repository.PublicRepository;
import com.ilhamrhmtkbr.domain.repository.StudentCartRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CourseDetailViewModel extends ViewModel {
    private final PublicRepository publicRepository;
    private final MemberRepository memberRepository;
    private final StudentCartRepository studentCartRepository;
    private final SingleLiveEvent<FormState<String>> studentCartFormResult = new SingleLiveEvent<>();
    private final SingleLiveEvent<FormState<String>> memberCourseLikeFormResult = new SingleLiveEvent<>();

    @Inject
    public CourseDetailViewModel(PublicRepository publicRepository, MemberRepository memberRepository, StudentCartRepository studentCartRepository) {
        this.publicRepository = publicRepository;
        this.memberRepository = memberRepository;
        this.studentCartRepository = studentCartRepository;
    }

    public void courseDetail(String courseId) {
        publicRepository.course(courseId);
    }

    public LiveData<Resource<Course>> getCourseResult() {
        return publicRepository.getCourseResult();
    }

    public void courseSection(String courseId) {
        publicRepository.courseSection(courseId);
    }

    public LiveData<Resource<List<Section>>> getSectionResult() {
        return publicRepository.getSectionResult();
    }

    public void storeLike(String courseId) {
        memberRepository.storeLike(courseId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                memberCourseLikeFormResult.postValue(state);
            }
        });
    }

    public void studentStoreCart(String courseId) {
        StudentCartStoreRequest request = new StudentCartStoreRequest();
        request.instructorCourseId = courseId;
        studentCartRepository.store(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                studentCartFormResult.postValue(state);
                if (state.isSuccess()) {
                    studentCartRepository.fetch("1", "desc");
                }
            }
        });
    }

    public LiveData<FormState<String>> getStudentCartFormResult() {
        return studentCartFormResult;
    }

    public LiveData<FormState<String>> getMemberCourseLikeFormResult() {
        return memberCourseLikeFormResult;
    }
}
