package com.ilhamrhmtkbr.presentation.student.profile;

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
import com.ilhamrhmtkbr.databinding.FragmentStudentProfileBinding;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.student.Cart;
import com.ilhamrhmtkbr.domain.model.student.Question;
import com.ilhamrhmtkbr.domain.model.student.Review;
import com.ilhamrhmtkbr.domain.model.student.Transaction;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private FragmentStudentProfileBinding binding;
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
        binding = FragmentStudentProfileBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupData();
        setupButtonOnclick();
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
            binding.category.setValue(authStateManager.getCurrentUserCategory());
        }
    }

    private void setupButtonOnclick() {
        binding.boxCarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.refreshCarts();
            }
        });
        binding.boxCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.refreshCertificates();
            }
        });
        binding.boxCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.refreshCourses();
            }
        });
        binding.boxQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.refreshQuestions();
            }
        });
        binding.boxReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.refreshReviews();
            }
        });
        binding.boxTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.refreshTransactions();
            }
        });
    }

    private void setupViewModel() {
        viewModel.getCarts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Cart>>>() {
            @Override
            public void onChanged(Resource<List<Cart>> listResource) {
                if (listResource.isLoading()) {
                    binding.cartsLoading.setVisibility(VISIBLE);
                    binding.carts.setVisibility(GONE);
                } else if (listResource.isSuccess()) {
                    binding.cartsLoading.setVisibility(GONE);
                    binding.carts.setVisibility(VISIBLE);
                    if (listResource.getData() != null) {
                        binding.carts.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.carts.setText("0");
                    }
                } else {
                    binding.carts.setText("0");
                }
            }
        });

        viewModel.getCertificates().observe(getViewLifecycleOwner(), new Observer<Resource<List<Certificate>>>() {
            @Override
            public void onChanged(Resource<List<Certificate>> listResource) {
                if (listResource.isLoading()) {
                    binding.certificatesLoading.setVisibility(VISIBLE);
                    binding.certificates.setVisibility(GONE);

                } else if (listResource.isSuccess()) {
                    binding.certificatesLoading.setVisibility(GONE);
                    binding.certificates.setVisibility(VISIBLE);
                    if (listResource.getData() != null) {
                        binding.certificates.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.certificates.setText("0");
                    }
                } else {
                    binding.certificates.setText("0");
                }
            }
        });

        viewModel.getCourses().observe(getViewLifecycleOwner(), new Observer<Resource<List<Course>>>() {
            @Override
            public void onChanged(Resource<List<Course>> listResource) {
                if (listResource.isLoading()) {
                    binding.coursesLoading.setVisibility(VISIBLE);
                    binding.courses.setVisibility(GONE);

                } else if (listResource.isSuccess()) {
                    binding.coursesLoading.setVisibility(GONE);
                    binding.courses.setVisibility(VISIBLE);
                    if (listResource.getData() != null) {
                        binding.courses.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.courses.setText("0");
                    }
                } else {
                    binding.courses.setText("0");
                }
            }
        });

        viewModel.getQuestions().observe(getViewLifecycleOwner(), new Observer<Resource<List<Question>>>() {
            @Override
            public void onChanged(Resource<List<Question>> listResource) {
                if (listResource.isLoading()) {
                    binding.questionsLoading.setVisibility(VISIBLE);
                    binding.questions.setVisibility(GONE);

                } else if (listResource.isSuccess()) {
                    binding.questionsLoading.setVisibility(GONE);
                    binding.questions.setVisibility(VISIBLE);
                    if (listResource.getData() != null) {
                        binding.questions.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.questions.setText("0");
                    }
                } else {
                    binding.questions.setText("0");
                }
            }
        });

        viewModel.getReviews().observe(getViewLifecycleOwner(), new Observer<Resource<List<Review>>>() {
            @Override
            public void onChanged(Resource<List<Review>> listResource) {
                if (listResource.isLoading()) {
                    binding.reviewsLoading.setVisibility(VISIBLE);
                    binding.reviews.setVisibility(GONE);

                } else if (listResource.isSuccess()) {
                    binding.reviewsLoading.setVisibility(GONE);
                    binding.reviews.setVisibility(VISIBLE);
                    if (listResource.getData() != null) {
                        binding.reviews.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.reviews.setText("0");
                    }
                } else {
                    binding.reviews.setText("0");
                }
            }
        });

        viewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<Resource<List<Transaction>>>() {
            @Override
            public void onChanged(Resource<List<Transaction>> listResource) {
                if (listResource.isLoading()) {
                    binding.transactionsLoading.setVisibility(VISIBLE);
                    binding.transactions.setVisibility(GONE);

                } else if (listResource.isSuccess()) {
                    binding.transactionsLoading.setVisibility(GONE);
                    binding.transactions.setVisibility(VISIBLE);
                    if (listResource.getData() != null) {
                        binding.transactions.setText(String.valueOf(listResource.getData().size()));
                    } else {
                        binding.transactions.setText("0");
                    }
                } else {
                    binding.transactions.setText("0");
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
