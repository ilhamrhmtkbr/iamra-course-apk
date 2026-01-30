package com.ilhamrhmtkbr.presentation.instructor.course;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.utils.tools.PermissionUtil;
import com.ilhamrhmtkbr.databinding.FragmentInstructorCourseBinding;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCourseResponse;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.ImageViewUtil;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseFragment extends Fragment {
    private FragmentInstructorCourseBinding binding;
    private CourseViewModel viewModel;
    private String formType;
    private String courseId;
    private String inputImageValue;

    private boolean hasNewImage = false;

    private InstructorCourseResponse currentCourse;

    private final String[] editor = {
            "javascript", "java", "python", "php",
            "c", "cpp", "ruby", "go", "swift", "kotlin",
            "typescript", "sql", "html", "css", "xml",
            "json", "yaml", "bash", "shell", "plaintext",
            "r", "dart", "rust", "dockerfile"};

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP && resultCode == android.app.Activity.RESULT_OK && data != null) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), resultUri);
                    String base64 = ImageViewUtil.convertBitmapToBase64(bitmap);
                    inputImageValue = "data:image/png;base64," + base64;
                    // ✅ Set flag bahwa user udah upload image baru
                    hasNewImage = true;
                    Glide.with(this).clear(binding.courseImage);
                    // ✅ Langsung set bitmap hasil crop
                    binding.courseImage.setImageBitmap(bitmap);
                    Log.d("InstructorCourse", "Image cropped successfully, size: " + bitmap.getByteCount());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("InstructorCourse", "Error cropping image: " + e.getMessage());
                    hasNewImage = false; // Reset flag kalau error
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            // Crop cancelled or failed
            hasNewImage = false;
            Log.d("InstructorCourse", "Crop cancelled or failed");
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            formType = TextUtil.capitalize(getArguments().getString("form_type"));
            assert formType != null;
            if (formType.equals("Edit")) {
                courseId = getArguments().getString("course_id");
            }
        }
        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorCourseBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
        setupButtonSubmit();
        setupViewModels();
    }

    // ✅ NEW: Method untuk load image lama dari server
    private void setupOldImage() {
        if (!hasNewImage && currentCourse != null) {
            String imageUrl = BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + currentCourse.image;

            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()   
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Jangan cache, biar selalu fresh
                    .skipMemoryCache(true) // Skip memory cache juga
                    .error(R.drawable.ic_custom_error)
                    .into(binding.courseImage);

            Log.d("InstructorCourse", "Loading old image from: " + imageUrl);
        }
    }

    private void setupElements() {
        binding.formType.setText(TextUtil.capitalize(formType));

        binding.inputPrice.getInput().setInputType(InputType.TYPE_CLASS_NUMBER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                editor
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.inputEditor.setAdapter(adapter);

        binding.buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtil.checkAndRequestStorage(requireActivity())) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickImageLauncher.launch(intent);
                }
            }
        });

        if (formType.equals("Edit")) {
            binding.buttonDelete.setVisibility(VISIBLE);
            viewModel.fetch(courseId);
        } else {
            binding.buttonDelete.setVisibility(GONE);
        }
    }

    private void setupButtonSubmit() {
        if (formType.equals("Edit")) {
            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showWarning(requireContext(),
                            getString(R.string.delete),
                            getString(R.string.are_you_sure_you_want_to_delete_this) + " " + getString(R.string.course) + " ?",
                            new DialogUtil.DialogCallback() {
                                @Override
                                public void onPositive() {
                                    viewModel.delete(courseId);
                                }
                            });
                }
            });

            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int levelId = binding.inputLevel.getCheckedRadioButtonId();
                    int statusId = binding.inputStatus.getCheckedRadioButtonId();
                    int visibilityId = binding.inputVisibility.getCheckedRadioButtonId();

                    if (levelId == -1) {
                        Toast.makeText(requireContext(), "Level must be selected", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (statusId == -1) {
                        Toast.makeText(requireContext(), "Status must be selected", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (visibilityId == -1) {
                        Toast.makeText(requireContext(), "Visibility must be selected", Toast.LENGTH_LONG).show();
                        return;
                    }

                    RadioButton levelRadio = binding.inputLevel.findViewById(levelId);
                    String inputLevel = levelRadio.getText().toString().toLowerCase();

                    RadioButton statusRadio = binding.inputStatus.findViewById(statusId);
                    String inputStatus = statusRadio.getText().toString().toLowerCase();

                    RadioButton visibilityRadio = binding.inputVisibility.findViewById(visibilityId);
                    String inputVisibility = visibilityRadio.getText().toString().toLowerCase();

                    viewModel.modify(
                            courseId,
                            binding.inputTitle.getValue(),
                            binding.inputDescription.getValue(),
                            inputImageValue,
                            binding.inputPrice.getValue(),
                            inputLevel,
                            inputStatus,
                            inputVisibility,
                            binding.inputNotes.getValue(),
                            binding.inputRequirements.getValue(),
                            binding.inputEditor.getSelectedItem().toString()
                    );
                }
            });
        } else {
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int levelId = binding.inputLevel.getCheckedRadioButtonId();
                    int statusId = binding.inputStatus.getCheckedRadioButtonId();
                    int visibilityId = binding.inputVisibility.getCheckedRadioButtonId();

                    if (levelId == -1) {
                        Toast.makeText(requireContext(), "Level must be selected", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (statusId == -1) {
                        Toast.makeText(requireContext(), "Status must be selected", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (visibilityId == -1) {
                        Toast.makeText(requireContext(), "Visibility must be selected", Toast.LENGTH_LONG).show();
                        return;
                    }

                    RadioButton levelRadio = binding.inputLevel.findViewById(levelId);
                    String inputLevel = levelRadio.getText().toString().toLowerCase();

                    RadioButton statusRadio = binding.inputStatus.findViewById(statusId);
                    String inputStatus = statusRadio.getText().toString().toLowerCase();

                    RadioButton visibilityRadio = binding.inputVisibility.findViewById(visibilityId);
                    String inputVisibility = visibilityRadio.getText().toString().toLowerCase();

                    viewModel.store(
                            binding.inputTitle.getValue(),
                            binding.inputDescription.getValue(),
                            inputImageValue,
                            binding.inputPrice.getValue(),
                            inputLevel,
                            inputStatus,
                            inputVisibility,
                            binding.inputNotes.getValue(),
                            binding.inputRequirements.getValue(),
                            binding.inputEditor.getSelectedItem().toString()
                    );
                }
            });
        }
    }

    private void setupViewModels() {
        binding.formType.setText(formType);

        if (formType.equals("Edit")) {
            viewModel.fetch(courseId);
            viewModel.getCourse().observe(getViewLifecycleOwner(), new Observer<Resource<InstructorCourseResponse>>() {
                @Override
                public void onChanged(Resource<InstructorCourseResponse> courseResponse) {
                    LoadingUtil.setup(courseResponse.isLoading(), binding.loading);
                    if (courseResponse.getData() != null) {
                        currentCourse = courseResponse.getData();
                        binding.inputTitle.setValue(currentCourse.title);
                        binding.inputDescription.setValue(currentCourse.description);
                        binding.inputPrice.setValue(currentCourse.price);
                        inputImageValue = currentCourse.image;

                        switch (currentCourse.level) {
                            case "junior":
                                binding.inputLevel.check(R.id.junior);
                                break;
                            case "middle":
                                binding.inputLevel.check(R.id.middle);
                                break;
                            case "advanced":
                                binding.inputLevel.check(R.id.advanced);
                                break;
                        }

                        switch (currentCourse.status) {
                            case "paid":
                                binding.inputStatus.check(R.id.paid);
                                break;
                            case "free":
                                binding.inputStatus.check(R.id.free);
                                break;
                        }

                        switch (currentCourse.visibility) {
                            case "private":
                                binding.inputVisibility.check(R.id.privatee);
                                break;
                            case "public":
                                binding.inputVisibility.check(R.id.publicc);
                                break;
                        }

                        binding.inputEditor.setSelection(getIndex(editor, currentCourse.editor));
                        binding.inputNotes.setValue(currentCourse.notes);
                        binding.inputRequirements.setValue(currentCourse.requirements);

                        // ✅ Load image lama setelah data course di-set
                        setupOldImage();
                    }
                }
            });
        }

        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrors(stringStringMap).run();
            }
        });

        viewModel.getCourseFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<String>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), handleAfterRequest());
            }
        });
    }

    private int getIndex(String[] items, String target) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(target)) {
                return i;
            }
        }
        return -1;
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
                        case "title" :
                            binding.inputTitle.getError().setVisibility(View.VISIBLE);
                            binding.inputTitle.getError().setText(error);
                            break;
                        case "description":
                            binding.inputDescription.getError().setVisibility(View.VISIBLE);
                            binding.inputDescription.getError().setText(error);
                            break;
                        case "image" :
                            binding.errorImage.setVisibility(View.VISIBLE);
                            binding.errorImage.setText(error);
                            break;
                        case "price":
                            binding.inputPrice.getError().setVisibility(View.VISIBLE);
                            binding.inputPrice.getError().setText(error);
                            break;
                        case "level":
                            binding.errorLevel.setVisibility(View.VISIBLE);
                            binding.errorLevel.setText(error);
                            break;
                        case "status":
                            binding.errorStatus.setVisibility(View.VISIBLE);
                            binding.errorStatus.setText(error);
                            break;
                        case "visibility":
                            binding.errorVisibility.setVisibility(View.VISIBLE);
                            binding.errorVisibility.setText(error);
                            break;
                        case "editor":
                            binding.errorEditor.setVisibility(View.VISIBLE);
                            binding.errorEditor.setText(error);
                            break;
                        case "notes":
                            binding.inputNotes.getError().setVisibility(View.VISIBLE);
                            binding.inputNotes.getError().setText(error);
                            break;
                        case "requirements":
                            binding.inputRequirements.getError().setVisibility(View.VISIBLE);
                            binding.inputRequirements.getError().setText(error);
                            break;

                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputTitle.getError().setText(null);
        binding.inputTitle.getError().setVisibility(View.GONE);
        binding.inputDescription.getError().setText(null);
        binding.inputDescription.getError().setVisibility(View.GONE);
        binding.errorImage.setText(null);
        binding.errorImage.setVisibility(View.GONE);
        binding.inputPrice.getError().setText(null);
        binding.inputPrice.getError().setVisibility(View.GONE);
        binding.errorLevel.setText(null);
        binding.errorLevel.setVisibility(View.GONE);
        binding.errorStatus.setText(null);
        binding.errorStatus.setVisibility(View.GONE);
        binding.errorVisibility.setText(null);
        binding.errorVisibility.setVisibility(View.GONE);
        binding.errorEditor.setText(null);
        binding.errorEditor.setVisibility(View.GONE);
        binding.inputNotes.getError().setText(null);
        binding.inputNotes.getError().setVisibility(View.GONE);
        binding.inputRequirements.getError().setText(null);
        binding.inputRequirements.getError().setVisibility(View.GONE);
    }

    private Runnable handleAfterRequest() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshCourses();
                NavController navController = NavHostFragment.findNavController(CourseFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_course, true)
                        .build();
                navController.navigate(R.id.nav_courses, null, navOptions);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
        currentCourse = null;
        inputImageValue = null;
        hasNewImage = false;
    }
}