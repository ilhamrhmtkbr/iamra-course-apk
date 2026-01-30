package com.ilhamrhmtkbr.presentation.student.transactiondetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.ilhamrhmtkbr.Ilhamrhmtkbr;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.databinding.FragmentStudentTransactionDetailBinding;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionDetailResponse;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.midtrans.sdk.uikit.api.model.TransactionResult;
import com.midtrans.sdk.uikit.internal.util.UiKitConstants;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransactionDetailFragment extends Fragment {
    private FragmentStudentTransactionDetailBinding binding;
    private TransactionDetailViewModel viewModel;
    private String orderId;
    private String snapToken;
    private ActivityResultLauncher<Intent> midtransLauncher;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(TransactionDetailViewModel.class);

        if (getArguments() != null) {
            orderId = getArguments().getString("order_id");
            snapToken = getArguments().getString("token");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentTransactionDetailBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        setupMidtransLauncher();
        setupListeners();
        setupViewModel();
    }

    private void setupMidtransLauncher() {
        midtransLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        TransactionResult transactionResult = result.getData()
                                .getParcelableExtra(UiKitConstants.KEY_TRANSACTION_RESULT);

                        if (transactionResult != null) {
                            switch (transactionResult.getStatus()) {
                                case UiKitConstants.STATUS_SUCCESS:
                                    Toast.makeText(getContext(), "Pembayaran berhasil!", Toast.LENGTH_LONG).show();
                                    afterSuccess();
                                    break;
                                case UiKitConstants.STATUS_PENDING:
                                    Toast.makeText(getContext(), "Menunggu pembayaran", Toast.LENGTH_LONG).show();
                                    break;
                                case UiKitConstants.STATUS_FAILED:
                                    Toast.makeText(getContext(), "Pembayaran gagal", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                }
        );
    }

    private void setupListeners() {
        binding.buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snapToken != null && !snapToken.isEmpty()) {
                    try {
                        Ilhamrhmtkbr.midtransApi.startPaymentUiFlow(
                                requireActivity(),
                                midtransLauncher,
                                snapToken
                        );
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Snap token tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.requestDelete(orderId);
            }
        });

        binding.buttonFreePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://simulator.sandbox.midtrans.com/openapi/va/index"));
                startActivity(intent);
            }
        });
    }

    private void setupViewModel() {
        viewModel.requestDetail(orderId);

        viewModel.getTransactionsDetail().observe(getViewLifecycleOwner(), new Observer<Resource<StudentTransactionDetailResponse>>() {
            @Override
            public void onChanged(Resource<StudentTransactionDetailResponse> studentTransactionDetailResponseResource) {
                LoadingUtil.setup(studentTransactionDetailResponseResource.isLoading(), binding.loading);
                if (studentTransactionDetailResponseResource.isSuccess() && studentTransactionDetailResponseResource.getData() != null) {
                    if (studentTransactionDetailResponseResource.getData().instructor_course_coupon_id != null){
                        binding.boxCoupon.setVisibility(View.VISIBLE);
                        String discount = studentTransactionDetailResponseResource.getData().instructor_course_coupon.discount + "%";
                        binding.couponDiscount.setText(discount);
                        binding.couponExpiry.setText(studentTransactionDetailResponseResource.getData().instructor_course_coupon.expiry_date);
                    } else {
                        binding.boxCoupon.setVisibility(View.GONE);
                    }

                    binding.orderId.setText(studentTransactionDetailResponseResource.getData().order_id);
                    binding.courseTitle.setText(studentTransactionDetailResponseResource.getData().instructor_course.title);
                    binding.coursePrice.setText(TextUtil.formatRupiah(Integer.parseInt(studentTransactionDetailResponseResource.getData().amount)));
                    binding.courseStatus.setText(studentTransactionDetailResponseResource.getData().status);
                    binding.courseCreatedAt.setText(studentTransactionDetailResponseResource.getData().created_at);
                }
            }
        });

        viewModel.getTransactionFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                LoadingUtil.setup(stringFormState.isLoading(), binding.loading);
                if (stringFormState.isSuccess()) {
                    afterSuccess();
                }
            }
        });
    }

    private void afterSuccess() {
        viewModel.refreshTransaction();
        viewModel.refreshCourses();
        NavController navController = NavHostFragment.findNavController(TransactionDetailFragment.this);
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_transactions, true)
                .build();
        navController.navigate(R.id.nav_courses, navOptions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        orderId = null;
    }
}
