package com.ilhamrhmtkbr.presentation.instructor.profile;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.databinding.FragmentInstructorProfileBinding;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.domain.model.instructor.Answer;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;
import com.ilhamrhmtkbr.domain.model.instructor.CoursesLike;
import com.ilhamrhmtkbr.domain.model.instructor.Earning;
import com.ilhamrhmtkbr.domain.model.instructor.Review;
import com.ilhamrhmtkbr.domain.model.instructor.Social;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private FragmentInstructorProfileBinding binding;
    private ProfileViewModel viewModel;
    @Inject
    AuthStateManager authStateManager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorProfileBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupData();
        setupViewModel();
    }

    private void setupData() {
        if (authStateManager.isLoggedIn()) {
            Glide.with(binding.image.getContext())
                    .load(BuildConfig.USER_PROFILE_IMAGE_URL + TextUtil.check(authStateManager.getCurrentUserImage(), ""))
                    .centerCrop()
                    .error(R.drawable.ic_custom_error)
                    .into(binding.image);
            binding.summary.setText(authStateManager.getCurrentUserSummary());
            binding.username.setValue(authStateManager.getCurrentUsername());
            binding.fullName.setValue(authStateManager.getCurrentUserFullName());
            binding.email.setValue(authStateManager.getCurrentUserEmail());
            binding.role.setValue(authStateManager.getCurrentUserRole());
            binding.address.setValue(authStateManager.getCurrentUserAddress());
            binding.dob.setValue(authStateManager.getCurrentUserDob());
        }
    }

    private void setupViewModel() {
        viewModel.getAnswers().observe(getViewLifecycleOwner(), new Observer<Resource<List<Answer>>>() {
            @Override
            public void onChanged(Resource<List<Answer>> listResource) {
                binding.answersLoading.setVisibility(listResource.isLoading() ? VISIBLE : GONE);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.answers.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.answers.setText("0");
                    }
                } else if (listResource.isError()) {
                    binding.answers.setText("0");
                }
            }
        });

        viewModel.getCoupons().observe(getViewLifecycleOwner(), new Observer<Resource<List<Coupon>>>() {
            @Override
            public void onChanged(Resource<List<Coupon>> listResource) {
                binding.couponsLoading.setVisibility(listResource.isLoading() ? VISIBLE : GONE);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.coupons.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.coupons.setText("0");
                    }
                } else if (listResource.isError()) {
                    binding.coupons.setText("0");
                }
            }
        });

        viewModel.getCoursesLikes().observe(getViewLifecycleOwner(), new Observer<Resource<List<CoursesLike>>>() {
            @Override
            public void onChanged(Resource<List<CoursesLike>> listResource) {
                binding.coursesLikesLoading.setVisibility(listResource.isLoading() ? VISIBLE : GONE);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.coursesLikes.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.coursesLikes.setText("0");
                    }
                } else if (listResource.isError()) {
                    binding.coursesLikes.setText("0");
                }
            }
        });

        viewModel.getEarnings().observe(getViewLifecycleOwner(), new Observer<Resource<List<Earning>>>() {
            @Override
            public void onChanged(Resource<List<Earning>> listResource) {
                binding.earningsLoading.setVisibility(listResource.isLoading() ? VISIBLE : GONE);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.earnings.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.earnings.setText("0");
                    }
                } else if (listResource.isError()) {
                    binding.earnings.setText("0");
                }
            }
        });

        viewModel.getReviews().observe(getViewLifecycleOwner(), new Observer<Resource<List<Review>>>() {
            @Override
            public void onChanged(Resource<List<Review>> listResource) {
                binding.reviewsLoading.setVisibility(listResource.isLoading() ? VISIBLE : GONE);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.reviews.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.reviews.setText("0");
                    }
                } else if (listResource.isError()) {
                    binding.reviews.setText("0");
                }
            }
        });

        viewModel.getSocials().observe(getViewLifecycleOwner(), new Observer<Resource<List<Social>>>() {
            @Override
            public void onChanged(Resource<List<Social>> listResource) {
                binding.socialsLoading.setVisibility(listResource.isLoading() ? VISIBLE : GONE);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.socials.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.socials.setText("0");
                    }
                } else if (listResource.isError()) {
                    binding.socials.setText("0");
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
