package com.ilhamrhmtkbr.presentation.member.additionalinfo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.utils.tools.PermissionUtil;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.databinding.FragmentUserMemberAdditionalInfoBinding;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAdditionalInfoRequest;
import com.ilhamrhmtkbr.presentation.utils.helper.ImageViewUtil;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdditionalInfoFragment extends Fragment {
    private AdditionalInfoViewModel viewModel;
    private FragmentUserMemberAdditionalInfoBinding binding;

    @Inject
    AuthStateManager authStateManager;

    private boolean isInitialized = false;

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        Uri tempCropDestination = Uri.fromFile(new File(requireContext().getCacheDir(), "cropped_image.jpg"));
                        UCrop.of(selectedImageUri, tempCropDestination)
                                .withAspectRatio(1, 1)
                                .withMaxResultSize(500, 500)
                                .start(requireContext(), this);
                    }
                }
            });

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    private AdditionalInfoViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(AdditionalInfoViewModel.class);
        }
        return viewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance) {
        binding = FragmentUserMemberAdditionalInfoBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        if (authStateManager.isLoggedIn()) {
            setupInitialData();
            setupViewModel();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setupListeners();
                    setupButtonOpenMap();
                    isInitialized = true;
                }
            }, 1000);
        }
    }

    private void setupInitialData() {
        if (getViewModel().hasNewImage()) {
            updatePreviewImage();
        } else {
            setupOldImage();
        }

        setupFormData();
    }

    private void setupOldImage() {
        String imageUrl = BuildConfig.USER_PROFILE_IMAGE_URL + TextUtil.check(authStateManager.getCurrentUserImage(), "");

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_custom_error)
                .into(binding.previewImage);

        viewModel.setCurrentImage(imageUrl);
    }

    private void updatePreviewImage() {
        String base64Image = getViewModel().getCurrentImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            Glide.with(this)
                    .load(base64Image)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.ic_custom_error)
                    .into(binding.previewImage);
        }
    }

    private void setupFormData() {
        Handler setupFirstName = new Handler();
        setupFirstName.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.inputFirstName.setValue(authStateManager.getCurrentUserFirstName());
            }
        }, 100);
        Handler setupMiddleName = new Handler();
        setupMiddleName.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.inputMiddleName.setValue(authStateManager.getCurrentUserMiddleName());
            }
        }, 300);
        Handler setuplastName = new Handler();
        setuplastName.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.inputLastName.setValue(authStateManager.getCurrentUserLastName());
            }
        }, 500);
        Handler setupDob = new Handler();
        setupDob.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.inputDob.setText(authStateManager.getCurrentUserDob());
            }
        }, 700);
        Handler setupAddress = new Handler();
        setupAddress.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.inputAddress.setValue(authStateManager.getCurrentUserAddress());
            }
        }, 900);
    }

    private void setupListeners() {
        binding.inputDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        R.style.Custom_DatePicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String date = year + "-" + (month + 1) + "-" + day;
                                binding.inputDob.setText(date);
                            }
                        },
                        year, month, day
                );

                datePickerDialog.show();
            }
        });

        binding.buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtil.checkAndRequestStorage(requireActivity())) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickImageLauncher.launch(intent);
                }
            }
        });

        binding.buttonSubmitAdditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberUpdateAdditionalInfoRequest request = new MemberUpdateAdditionalInfoRequest(
                        binding.inputFirstName.getValue(),
                        binding.inputMiddleName.getValue(),
                        binding.inputLastName.getValue(),
                        getViewModel().getCurrentImage(),
                        binding.inputAddress.getValue(),
                        binding.inputDob.getText().toString().trim()
                );

                getViewModel().request(request);
            }
        });
    }

    private void setupButtonOpenMap() {
        binding.buttonOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtil.checkAndRequestLocation(requireActivity())) {
                    AdditionalInfoBottomSheetMap bottomSheet = AdditionalInfoBottomSheetMap.newInstance();

                    bottomSheet.setOnLocationSelectedListener(new AdditionalInfoBottomSheetMap.OnLocationSelectedListener() {
                        @Override
                        public void onLocationSelected(String address, double lat, double lng) {
                            if (binding.inputAddress != null) {
                                binding.inputAddress.setValue(address);
                                Toast.makeText(requireContext(), "Alamat diperbarui!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    bottomSheet.show(getChildFragmentManager(), "MapBottomSheet");
                }
            }
        });
    }

    private void setupViewModel() {
        getViewModel().getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrors(stringStringMap).run();
            }
        });

        getViewModel().getAdditionalInfoFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), afterSuccess());
            }
        });
    }

    private Runnable showErrors(Map<String, String> errors) {
        return new Runnable() {
            @Override
            public void run() {
                if (errors.isEmpty()) {
                    return;
                }
                removeErrors();
                DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));

                for (Map.Entry<String, String> item : errors.entrySet()) {
                    String field = item.getKey();
                    String error = item.getValue();

                    switch (field) {
                        case "image":
                            binding.errorImage.setVisibility(View.VISIBLE);
                            binding.errorImage.setText(error);
                            break;
                        case "first_name":
                            binding.inputFirstName.getError().setVisibility(View.VISIBLE);
                            binding.inputFirstName.getError().setText(error);
                            break;
                        case "middle_name":
                            binding.inputMiddleName.getError().setVisibility(View.VISIBLE);
                            binding.inputMiddleName.getError().setText(error);
                            break;
                        case "last_name":
                            binding.inputLastName.getError().setVisibility(View.VISIBLE);
                            binding.inputLastName.getError().setText(error);
                            break;
                        case "dob":
                            binding.errorDob.setVisibility(View.VISIBLE);
                            binding.errorDob.setText(error);
                            break;
                        case "address":
                            binding.inputAddress.getError().setVisibility(View.VISIBLE);
                            binding.inputAddress.getError().setText(error);
                            break;
                    }
                }
            }
        };
    }

    public Runnable afterSuccess() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshUserData();
            }
        };
    }

    private void removeErrors() {
        binding.errorImage.setVisibility(View.GONE);
        binding.errorImage.setText(null);
        binding.errorDob.setVisibility(View.GONE);
        binding.errorDob.setText(null);
        binding.inputFirstName.getError().setVisibility(View.GONE);
        binding.inputFirstName.getError().setText(null);
        binding.inputMiddleName.getError().setVisibility(View.GONE);
        binding.inputMiddleName.getError().setText(null);
        binding.inputLastName.getError().setVisibility(View.GONE);
        binding.inputLastName.getError().setText(null);
        binding.inputAddress.getError().setVisibility(View.GONE);
        binding.inputAddress.getError().setText(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP && resultCode == android.app.Activity.RESULT_OK && data != null) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), resultUri);
                    String base64 = ImageViewUtil.convertBitmapToBase64(bitmap);

                    // ✅ Simpan ke ViewModel
                    getViewModel().setCurrentImage("data:image/png;base64," + base64);
                    getViewModel().setHasNewImage(true); // ✅ Set flag

                    // ✅ Update preview langsung
                    if (binding.previewImage != null) {
                        Glide.with(this).clear(binding.previewImage);
                        binding.previewImage.setImageBitmap(bitmap);
                        Log.d("AdditionalInfo", "Image cropped successfully, size: " + bitmap.getByteCount());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("AdditionalInfo", "Error cropping image: " + e.getMessage());
                    getViewModel().resetImage();
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            getViewModel().resetImage();
            Log.d("AdditionalInfo", "Crop cancelled or failed");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInitialized && getViewModel().hasNewImage()) {
            updatePreviewImage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitialized = false;
        binding = null; // ✅ Sudah di-null
    }
}