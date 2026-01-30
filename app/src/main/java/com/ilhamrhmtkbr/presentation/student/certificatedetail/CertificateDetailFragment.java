package com.ilhamrhmtkbr.presentation.student.certificatedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.utils.tools.PermissionUtil;
import com.ilhamrhmtkbr.databinding.FragmentStudentCertificateDetailBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.domain.model.student.CertificateDetail;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.io.File;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CertificateDetailFragment extends Fragment {
    private FragmentStudentCertificateDetailBinding binding;
    private CertificateDetailViewModel viewModel;
    private String certificateId;
    private CertificateDetailAdapter certificateDetailAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(CertificateDetailViewModel.class);

        if (getArguments() != null) {
            certificateId = getArguments().getString("certificate_id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentCertificateDetailBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        viewModel.request(certificateId);
        setupRecyclerView();
        setupListeners();
        observeViewModel();
    }

    private void setupRecyclerView() {
        certificateDetailAdapter = new CertificateDetailAdapter();
        binding.rv.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(33));
        binding.rv.setAdapter(certificateDetailAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupListeners() {
        binding.buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtil.checkAndRequestStorage(requireActivity())) {
                    viewModel.download(certificateId, requireContext());
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getCertificateDetail().observe(getViewLifecycleOwner(), new Observer<Resource<CertificateDetail>>() {
            @Override
            public void onChanged(Resource<CertificateDetail> certificateDetailResource) {
                LoadingUtil.setup(certificateDetailResource.isLoading(), binding.loading);
                if (certificateDetailResource.getData() != null) {
                    Course course = certificateDetailResource.getData().getCourse();
                    List<Section> sections = certificateDetailResource.getData().getSections();
                    binding.courseTitle.setText(course.getTitle());
                    binding.courseDesc.setText(course.getDescription());
                    certificateDetailAdapter.setData(sections);
                }
            }
        });

        viewModel.getDownloadResult().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                LoadingUtil.setup(stringFormState.isLoading(), binding.loading);
                binding.buttonDownload.setEnabled(!stringFormState.isLoading());

                if (stringFormState.isLoading()) {
                    binding.buttonDownload.setText("Downloading...");
                } else {
                    binding.buttonDownload.setText("Download Certificate");
                }

                if (stringFormState.isSuccess() && stringFormState.getData() != null) {
                    Toast.makeText(requireContext(), "Download berhasil!", Toast.LENGTH_SHORT).show();
                    openPdfFile(stringFormState.getData());
                } else if (stringFormState.isError()) {
                    Toast.makeText(requireContext(), stringFormState.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openPdfFile(String filePath) {
        try {
            Uri fileUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Path dari MediaStore (uri string)
                fileUri = Uri.parse(filePath);
            } else {
                // Path dari File
                File file = new File(filePath);
                fileUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().getPackageName() + ".provider",
                        file
                );
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "Gagal membuka file: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
