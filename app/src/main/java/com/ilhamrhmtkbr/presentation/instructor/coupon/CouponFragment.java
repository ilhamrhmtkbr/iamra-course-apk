package com.ilhamrhmtkbr.presentation.instructor.coupon;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.databinding.FragmentInstructorCouponBinding;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;

import java.util.Calendar;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CouponFragment extends Fragment {
    private FragmentInstructorCouponBinding binding;
    private CouponViewModel viewModel;
    private String formType;
    private String couponId;
    private String courseId;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            formType = getArguments().getString("form_type");
            assert formType != null;
            if (formType.equals("edit")) {
                couponId = getArguments().getString("coupon_id");
            } else {
                courseId = getArguments().getString("course_id");
            }
        }

        viewModel = new ViewModelProvider(this).get(CouponViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorCouponBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
        setupButtonSubmit();
        observeViewModels();
    }

    private void setupElements() {
        binding.formType.setText(TextUtil.capitalize(formType));
        binding.inputDiscount.getInput().setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.inputMaxRedemptions.getInput().setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.inputExpiryDate.getInput().setFocusable(false);
        binding.inputExpiryDate.getInput().setClickable(true);
        binding.inputExpiryDate.getInput().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        (DatePicker view, int y, int m, int d) -> {
                            String date = y + "/" + (m + 1) + "/" + d;
                            binding.inputExpiryDate.setValue(date);
                        },
                        year, month, day
                );

                datePickerDialog.show();
            }
        });
    }

    private void setupButtonSubmit() {
        if (formType.equals("edit")) {
            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.delete(couponId);
                }
            });

            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.modify(
                            couponId,
                            binding.inputDiscount.getValue(),
                            binding.inputExpiryDate.getValue(),
                            binding.inputMaxRedemptions.getValue()
                    );
                }
            });
        } else {
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.store(
                            binding.inputDiscount.getValue(),
                            courseId,
                            binding.inputExpiryDate.getValue(),
                            binding.inputMaxRedemptions.getValue()
                    );
                }
            });
        }
    }

    private void observeViewModels() {
        if (formType.equals("edit")) {
            viewModel.show(couponId);
            binding.couponDetail.setVisibility(VISIBLE);
            viewModel.getCouponResult().observe(getViewLifecycleOwner(), new Observer<Resource<Coupon>>() {
                @Override
                public void onChanged(Resource<Coupon> couponResource) {
                    LoadingUtil.setup(couponResource.isLoading(), binding.loading);
                    if (couponResource.getData() != null) {
                        Coupon coupon = couponResource.getData();
                        String discount = " : " + coupon.getDiscount();
                        String expiryData = " : " + coupon.getExpiryDate();
                        String maxRedemptions = " : " + coupon.getMaxRedemptions();

                        binding.discount.setText(discount);
                        binding.expiryDate.setText(expiryData);
                        binding.maxRedemptions.setText(maxRedemptions);

                        binding.inputDiscount.setValue(coupon.getDiscount());
                        binding.inputExpiryDate.setValue(coupon.getExpiryDate());
                        binding.inputMaxRedemptions.setValue(coupon.getMaxRedemptions());
                        binding.courseTitle.setValue(coupon.getInstructorCourseTitle());
                        binding.courseStatus.setValue(coupon.getInstructorCourseStatus());
                    }
                }
            });
        } else {
            binding.couponDetail.setVisibility(GONE);
        }

        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrors(stringStringMap).run();
            }
        });

        viewModel.getCouponFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<String>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), handleAfterRequest());
            }
        });
    }

    private Runnable showErrors(Map<String, String> errors) {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                if (errors.isEmpty()) {
                    return;
                }

                DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));

                for (Map.Entry<String, String> item : errors.entrySet()) {
                    String field = item.getKey();
                    String error = item.getValue();

                    switch (field) {
                        case "discount":
                            binding.inputDiscount.getError().setText(error);
                            binding.inputDiscount.getError().setVisibility(VISIBLE);
                            break;
                        case "expiry_date":
                            binding.inputExpiryDate.getError().setText(error);
                            binding.inputExpiryDate.getError().setVisibility(VISIBLE);
                            break;
                        case "max_redemptions":
                            binding.inputMaxRedemptions.getError().setText(error);
                            binding.inputMaxRedemptions.getError().setVisibility(VISIBLE);
                            break;
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputDiscount.getError().setText(null);
        binding.inputDiscount.getError().setVisibility(GONE);
        binding.inputExpiryDate.getError().setText(null);
        binding.inputExpiryDate.getError().setVisibility(GONE);
        binding.inputMaxRedemptions.getError().setText(null);
        binding.inputMaxRedemptions.getError().setVisibility(GONE);
    }

    private Runnable handleAfterRequest() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                NavController navController = NavHostFragment.findNavController(CouponFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_coupon, true)
                        .build();
                navController.navigate(R.id.nav_coupons, null, navOptions);
                viewModel.getCouponsRepository().fetch("1", "desc");
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
