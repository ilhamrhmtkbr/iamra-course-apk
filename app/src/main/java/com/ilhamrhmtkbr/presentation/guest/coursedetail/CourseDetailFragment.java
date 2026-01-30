package com.ilhamrhmtkbr.presentation.guest.coursedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;
import com.ilhamrhmtkbr.databinding.FragmentPublicCourseDetailBinding;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.ilhamrhmtkbr.presentation.student.StudentActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseDetailFragment extends Fragment {
    @Inject
    AuthStateManager authStateManager;
    private FragmentPublicCourseDetailBinding binding;
    private CourseDetailViewModel viewModel;
    private String courseId;
    private CourseDetailSectionBottomSheet bottomSheet;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        viewModel = new ViewModelProvider(this).get(CourseDetailViewModel.class);
        courseId = getArguments() != null ? getArguments().getString("course_id") : null;

        if (courseId != null) {
            viewModel.courseDetail(courseId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublicCourseDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        observeCourseSection();
        observeCourseDetail();
        setupButtonSection();
        setupButtonLike(courseId);
        setupButtonStudentAddToCart();
    }

    private void observeCourseDetail() {
        viewModel.getCourseResult().observe(getViewLifecycleOwner(), new Observer<Resource<Course>>() {
            @Override
            public void onChanged(Resource<Course> courseResource) {
                if (courseResource.isSuccess() && courseResource.getData() != null) {
                    Course course = courseResource.getData();

                    Glide.with(binding.courseImage.getContext())
                            .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + course.getImage())
                            .centerCrop()
                            .into(binding.courseImage);


                    binding.courseTitle.setText(course.getTitle());
                    binding.courseDesc.setText(course.getDescription());
                    binding.courseStatus.setValue(course.getStatus());
                    binding.courseInstructor.setValue(course.getInstructorId());
                    binding.coursePrice.setValue(course.getPrice());
                    binding.courseLevel.setValue(course.getLevel());
                    binding.courseNotes.setText(course.getNotes());
                    binding.courseRequirements.setText(course.getRequirements());
                    binding.courseLikeNum.setText(course.getLikes());

                    if (course.isLikes()) {
                        binding.courseLike.setImageResource(R.drawable.ic_member_unlike);
                    } else {
                        binding.courseLike.setImageResource(R.drawable.ic_member_like);
                    }
                }
            }
        });

        viewModel.getStudentCartFormResult().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                LoadingUtil.setup(stringFormState.isLoading(), binding.loading);
                if (stringFormState.isSuccess()) {
                    DialogUtil.showConfirmation(requireContext(), getString(R.string.add_to_cart), stringFormState.getMessage(), getString(R.string.see_cart), "Ok", new DialogUtil.DialogCallback() {
                        @Override
                        public void onPositive() {
                            RedirectUtil.redirectToActivityWithExtra(requireContext(), StudentActivity.class, "navigate_to", "carts");
                        }
                    });
                } else if (stringFormState.isError()) {
                    Toast.makeText(requireContext(), stringFormState.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getMemberCourseLikeFormResult().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                LoadingUtil.setup(stringFormState.isLoading(), binding.loading);
                if (stringFormState.isSuccess()) {
                    DialogUtil.showSuccess(requireContext(), getString(R.string.courses_likes), stringFormState.getMessage(), null);
                } else {
                    Toast.makeText(requireContext(), stringFormState.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupButtonLike(String courseId) {
        binding.courseLike.setOnClickListener(v -> {
            if (!authStateManager.isLoggedIn()) {
                Toast.makeText(getContext(), R.string.warning_no_login, Toast.LENGTH_SHORT).show();
                authStateManager.logout();
            } else if (!authStateManager.getCurrentUserRole().equals(SessionRepository.STUDENT)) {
                DialogUtil.showSuccess(requireContext(), getString(R.string.authentication), getString(R.string.fragment_public_course_detail_like_warning), null);
            } else {
                viewModel.storeLike(courseId);
            }
        });
    }

    private void setupButtonStudentAddToCart() {
        binding.buttonAddToCart.setOnClickListener(v -> {
            if (!authStateManager.isLoggedIn()) {
                Toast.makeText(getContext(), R.string.warning_no_login, Toast.LENGTH_SHORT).show();
                authStateManager.logout();
            } else if (!authStateManager.getCurrentUserRole().equals(SessionRepository.STUDENT)) {
                Toast.makeText(getContext(), R.string.fragment_public_course_detail_add_cart_warning, Toast.LENGTH_SHORT).show();
            } else {
                viewModel.studentStoreCart(courseId);
            }
        });
    }

    private void setupButtonSection() {
        binding.buttonSeeSection.setOnClickListener(v -> {
            bottomSheet.show(getChildFragmentManager(), "CourseDetailSectionBottomSheet");
        });
    }

    private void observeCourseSection() {
        viewModel.courseSection(courseId);
        viewModel.getSectionResult().observe(getViewLifecycleOwner(), new Observer<Resource<List<Section>>>() {
            @Override
            public void onChanged(Resource<List<Section>> listResource) {
                LoadingUtil.setup(listResource.isLoading(), binding.loading);
                bottomSheet = new CourseDetailSectionBottomSheet();
                if (listResource.isSuccess() && listResource.getData() != null) {
                    bottomSheet.setSections(listResource.getData());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}