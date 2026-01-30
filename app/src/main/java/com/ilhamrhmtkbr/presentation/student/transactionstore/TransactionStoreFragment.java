package com.ilhamrhmtkbr.presentation.student.transactionstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.databinding.FragmentStudentTransactionStoreBinding;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionCheckCouponResponse;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransactionStoreFragment extends Fragment {
    private FragmentStudentTransactionStoreBinding binding;
    private TransactionStoreViewModel viewModel;
    private String couponId = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        viewModel = new ViewModelProvider(this).get(TransactionStoreViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentTransactionStoreBinding.inflate(getLayoutInflater(), viewGroup, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupDetail();
        setupListeners();
        setupViewModel();
    }

    public void setupDetail() {
        assert getArguments() != null;
        Glide.with(binding.courseImage.getContext())
                .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + getArguments().getString("course_image"))
                .centerCrop()
                .into(binding.courseImage);
        binding.courseId.setValue(getArguments().getString("course_id"));
        binding.courseId.setReadOnly(false);
        binding.courseTitle.setText(getArguments().getString("course_title"));
        binding.courseDesc.setText(getArguments().getString("course_desc"));
        binding.coursePrice.setValue(getArguments().getString("course_price"));
        binding.courseLevel.setValue(TextUtil.capitalize(getArguments().getString("course_level")));
        binding.courseStatus.setValue(TextUtil.capitalize(getArguments().getString("course_status")));
    }

    public void setupListeners() {
        binding.buttonCheckCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.checkCoupon(binding.courseId.getValue());
            }
        });

        binding.buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.storeTransaction(binding.courseId.getValue(), couponId);
            }
        });
    }

    public void setupViewModel() {
        viewModel.getCouponFormState().observe(getViewLifecycleOwner(), new Observer<FormState<StudentTransactionCheckCouponResponse>>() {
            @Override
            public void onChanged(FormState<StudentTransactionCheckCouponResponse> studentTransactionCheckCouponResponseFormState) {
                LoadingUtil.setup(studentTransactionCheckCouponResponseFormState.isLoading(), binding.loading);
                if (studentTransactionCheckCouponResponseFormState.isSuccess()) {
                    if (studentTransactionCheckCouponResponseFormState.getData() != null) {
                        couponId = studentTransactionCheckCouponResponseFormState.getData().id;
                        binding.coupon.setVisibility(View.VISIBLE);
                        String discount = studentTransactionCheckCouponResponseFormState.getData().discount + '%';
                        binding.discount.setText(discount);
                        binding.maxRedemptions.setText(studentTransactionCheckCouponResponseFormState.getData().max_redemptions);
                        binding.expiryDate.setText(studentTransactionCheckCouponResponseFormState.getData().expiry_date);
                    }
                    DialogUtil.showSuccessSnackbar(requireView(), studentTransactionCheckCouponResponseFormState.getMessage());
                } else if (studentTransactionCheckCouponResponseFormState.isError()) {
                    DialogUtil.showErrorSnackbar(requireView(), studentTransactionCheckCouponResponseFormState.getMessage());
                }
            }
        });

        viewModel.getTransactionFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                LoadingUtil.setup(stringFormState.isLoading(), binding.loading);
                if (stringFormState.isSuccess()) {
                    viewModel.refreshTransaction();
                    DialogUtil.showSuccessSnackbar(requireView(), stringFormState.getMessage());
                    NavController navController = NavHostFragment.findNavController(TransactionStoreFragment.this);
                    navController.navigate(R.id.nav_transactions);
                } else if (stringFormState.isError()) {
                    DialogUtil.showErrorSnackbar(requireView(), stringFormState.getMessage());
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
