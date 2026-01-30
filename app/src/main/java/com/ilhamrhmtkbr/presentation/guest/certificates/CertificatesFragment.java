package com.ilhamrhmtkbr.presentation.guest.certificates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.data.mapper.guest.CertificateMapper;
import com.ilhamrhmtkbr.databinding.FragmentPublicCertificatesBinding;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCertificateVerifyResponse;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CertificatesFragment extends Fragment {
    private FragmentPublicCertificatesBinding binding;
    private CertificatesViewModel viewModel;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(CertificatesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublicCertificatesBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            String certificateId = getArguments().getString("certificate_id");
            if (certificateId != null) {
                binding.inputCertId.setValue(certificateId);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        setupListeners();
        observerViewModel();
    }

    private void observerViewModel() {
        viewModel.getCertificateState().observe(getViewLifecycleOwner(), new Observer<FormState<PublicCertificateVerifyResponse>>() {
            @Override
            public void onChanged(FormState<PublicCertificateVerifyResponse> state) {
                LoadingUtil.setup(state.isLoading(), binding.loading);
                if (state.isSuccess()) {
                    if (state.getData() != null) {
                        Certificate data = CertificateMapper.fromResponse(state.getData());
                        binding.certificate.setVisibility(View.VISIBLE);
                        binding.certificateStudent.setText(data.getStudentName());
                        binding.certificateTitle.setText(data.getInstructorCourseTitle());
                        binding.certificateAt.setText(data.getCreatedAt());
                    } else {
                        binding.certificate.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.fragment_public_certificates_warning, Toast.LENGTH_SHORT).show();
                    }
                }

                if (state.isValidationError()) {
                    showError(state.getValidationErrors());
                }
            }
        });

        viewModel.getValidationFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showError(stringStringMap);
            }
        });
    }

    private void setupListeners() {
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.verifyCertificate(binding.inputCertId.getValue());
            }
        });
    }

    private void showError(Map<String, String> errors) {
        if (errors.isEmpty()) {
            binding.inputCertId.getError().setVisibility(View.GONE);
        } else {
            DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));
            binding.inputCertId.getError().setVisibility(View.VISIBLE);
            binding.inputCertId.getError().setText(errors.entrySet().iterator().next().getValue());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
