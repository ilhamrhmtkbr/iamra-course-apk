package com.ilhamrhmtkbr.presentation.member.roleregister;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.utils.tools.PermissionUtil;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.instructor.InstructorActivity;
import com.ilhamrhmtkbr.presentation.student.StudentActivity;
import com.ilhamrhmtkbr.presentation.utils.component.ButtonSubmit;
import com.ilhamrhmtkbr.presentation.utils.component.InputText;
import com.ilhamrhmtkbr.databinding.FragmentUserMemberRoleRegisterBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RoleRegisterFragment extends Fragment {
    private RoleRegisterViewModel viewModel;
    private FragmentUserMemberRoleRegisterBinding binding;
    private EditText inputResume;
    private Uri instructorResume;
    private ConstraintLayout formStudent;
    private ConstraintLayout formInstructor;
    private TextView buttonFormStudent, buttonFormInstructor;

    // TAMBAHKAN: Variable untuk menyimpan form yang sedang aktif
    private static final String KEY_ACTIVE_FORM = "active_form";
    private static final int FORM_STUDENT = 0;
    private static final int FORM_INSTRUCTOR = 1;
    private int activeForm = FORM_STUDENT; // Default student

    @Inject
    AuthStateManager authStateManager;
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    instructorResume = result.getData().getData();
                    if (instructorResume != null) {
                        String fileName = getFileName(instructorResume);
                        String mimeType = getContext().getContentResolver().getType(instructorResume);

                        // Log untuk debug
                        android.util.Log.d("FILE_PICKER", "File: " + fileName);
                        android.util.Log.d("FILE_PICKER", "MIME Type: " + mimeType);

                        inputResume.setText(fileName);
                    }
                }
            });

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(requireActivity()).get(RoleRegisterViewModel.class);

        // TAMBAHKAN: Restore state jika ada
        if (bundle != null) {
            activeForm = bundle.getInt(KEY_ACTIVE_FORM, FORM_STUDENT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserMemberRoleRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        if (authStateManager.isLoggedIn()) {
            setupBinding();
            setupFormData();
            setupListeners();

            // TAMBAHKAN: Restore form yang terakhir aktif
            restoreActiveForm();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (authStateManager.isLoggedIn()) {
            setupBinding();
            // HAPUS setupFormData() dari sini - jangan reset form
            setupListeners();

            // TAMBAHKAN: Restore form yang terakhir aktif
            restoreActiveForm();
        }
    }

    // TAMBAHKAN: Method untuk save state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ACTIVE_FORM, activeForm);
    }

    private void setupBinding() {
        formStudent = binding.formStudent;
        formInstructor = binding.formInstructor;
        buttonFormStudent = binding.buttonFormStudent;
        buttonFormInstructor = binding.buttonFormInstructor;
    }

    private void setupFormData() {
        String role = authStateManager.getCurrentUserRole();

        switch (role) {
            case SessionRepository.USER:
                // Jangan langsung set visibility di sini
                // Biarkan activeForm yang menentukan
                setupFormStudent();
                setupFormInstructor();
                break;
            case SessionRepository.STUDENT:
                formStudent.setVisibility(View.VISIBLE);
                formInstructor.setVisibility(View.GONE);
                buttonFormStudent.setVisibility(View.VISIBLE);
                buttonFormInstructor.setVisibility(View.GONE);
                activeForm = FORM_STUDENT; // Set active form
                setupFormStudent();
                break;
            case SessionRepository.INSTRUCTOR:
                formStudent.setVisibility(View.GONE);
                formInstructor.setVisibility(View.VISIBLE);
                buttonFormStudent.setVisibility(View.GONE);
                buttonFormInstructor.setVisibility(View.VISIBLE);
                activeForm = FORM_INSTRUCTOR; // Set active form
                setupFormInstructor();
                break;
        }
    }

    // TAMBAHKAN: Method untuk restore form yang aktif
    private void restoreActiveForm() {
        if (activeForm == FORM_INSTRUCTOR) {
            showInstructorForm();
        } else {
            showStudentForm();
        }
    }

    // TAMBAHKAN: Method untuk show student form
    private void showStudentForm() {
        formStudent.setVisibility(View.VISIBLE);
        formInstructor.setVisibility(View.GONE);
        buttonFormStudent.setTextColor(requireContext().getColor(R.color.blue_color));
        buttonFormInstructor.setTextColor(requireContext().getColor(R.color.link_color));
        activeForm = FORM_STUDENT;
    }

    // TAMBAHKAN: Method untuk show instructor form
    private void showInstructorForm() {
        formStudent.setVisibility(View.GONE);
        formInstructor.setVisibility(View.VISIBLE);
        buttonFormStudent.setTextColor(requireContext().getColor(R.color.link_color));
        buttonFormInstructor.setTextColor(requireContext().getColor(R.color.blue_color));
        activeForm = FORM_INSTRUCTOR;
    }

    private void setupListeners() {
        buttonFormStudent.setOnClickListener(v -> {
            showStudentForm(); // Gunakan method baru
        });

        buttonFormInstructor.setOnClickListener(v -> {
            showInstructorForm(); // Gunakan method baru
        });
    }

    private void setupFormStudent() {
        RadioGroup inputCategory = binding.inputCategoryStudent;
        InputText inputSummaryStudent = binding.inputSummaryStudent;
        ButtonSubmit buttonSubmitStudent = binding.buttonSubmitStudent;

        if (authStateManager.getCurrentUserRole().equals("student")) {
            String category = authStateManager.getCurrentUserCategory();
            switch (category) {
                case "learner":
                    inputCategory.check(R.id.learner);
                    break;
                case "jobless":
                    inputCategory.check(R.id.jobless);
                    break;
                case "employee":
                    inputCategory.check(R.id.employee);
                    break;
            }
            inputSummaryStudent.setValue(authStateManager.getCurrentUserSummary());
        }

        buttonSubmitStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = inputCategory.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    Toast.makeText(requireContext(), getString(R.string.fragment_member_set_role_category_warning), Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton selectedRadioButton = inputCategory.findViewById(selectedId);
                    String selectedCategory = selectedRadioButton.getText().toString();

                    viewModel.registerAsStudent(selectedCategory, inputSummaryStudent.getValue());
                }
            }
        });

        viewModel.getStudentFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrorsStudent(stringFormState.getValidationErrors()), goToStudentActivity());
            }
        });

        viewModel.getValidationErrorsFrontendStudent().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrorsStudent(stringStringMap).run();
            }
        });
    }

    private Runnable goToStudentActivity() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshUserData();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(requireActivity(), StudentActivity.class);
                        startActivity(intent);
                    }
                }, 2000);
            }
        };
    }

    private void setupFormInstructor() {
        inputResume = binding.inputResume;
        TextView errorResume = binding.errorResume;
        InputText inputSummaryInstructor = binding.inputSummaryInstructor;
        ButtonSubmit buttonSubmitInstructor = binding.buttonSubmitInstructor;

        if (authStateManager.getCurrentUserRole().equals("instructor")) {
            inputSummaryInstructor.setValue(authStateManager.getCurrentUserSummary());
            inputResume.setText(R.string.fragment_member_set_role_file_exist);
        }

        inputResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                filePickerLauncher.launch(intent);
            }
        });

        buttonSubmitInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instructorResume == null) {
                    errorResume.setText(R.string.fragment_member_set_role_resume_warning);
                    errorResume.setVisibility(View.VISIBLE);
                    return;
                }

                try {
                    // Validasi MIME type dari ContentResolver
                    String mimeType = getContext().getContentResolver().getType(instructorResume);
                    android.util.Log.d("SUBMIT_DEBUG", "ContentResolver MIME type: " + mimeType);

                    if (mimeType == null || !mimeType.equals("application/pdf")) {
                        errorResume.setText("File harus berformat PDF");
                        errorResume.setVisibility(View.VISIBLE);
                        return;
                    }

                    // Buat file temporary dengan ekstensi .pdf
                    File tempFile = new File(getContext().getCacheDir(), "temp_resume.pdf");

                    // Copy file dari URI ke temp file
                    InputStream inputStream = getContext().getContentResolver().openInputStream(instructorResume);
                    FileOutputStream outputStream = new FileOutputStream(tempFile);

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    long totalBytes = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                    }

                    outputStream.close();
                    inputStream.close();

                    // Validasi ukuran
                    if (tempFile.length() > 512L * 1024) {
                        tempFile.delete(); // Clean up
                        errorResume.setText(R.string.fragment_member_set_role_file_max_warning);
                        errorResume.setVisibility(View.VISIBLE);
                        return;
                    }

                    android.util.Log.d("MULTIPART_DEBUG", "=== SENDING MULTIPART ===");
                    android.util.Log.d("MULTIPART_DEBUG", "Role: " + "instructor");
                    android.util.Log.d("MULTIPART_DEBUG", "Summary: " + inputSummaryInstructor.getValue());
                    android.util.Log.d("MULTIPART_DEBUG", "File name: " + tempFile.getName());
                    android.util.Log.d("MULTIPART_DEBUG", "File size: " + tempFile.length());
                    android.util.Log.d("MULTIPART_DEBUG", "File exists: " + tempFile.exists());

                    viewModel.registerAsInstructor("instructor", inputSummaryInstructor.getValue(), tempFile);

                    // Optional: Delete temp file after upload (atau biarkan di cache)
                    // tempFile.delete();

                } catch (Exception e) {
                    android.util.Log.e("SUBMIT_DEBUG", "Error: " + e.getMessage(), e);
                    errorResume.setText(R.string.fragment_member_set_role_file_read_warning);
                    errorResume.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getInstructorFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrorsInstructor(stringFormState.getValidationErrors()), goToInstructorActivity());
            }
        });

        viewModel.getValidationErrorsFrontendInstructor().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrorsInstructor(stringStringMap).run();
            }
        });
    }

    private Runnable goToInstructorActivity() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshUserData();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(requireActivity(), InstructorActivity.class);
                        startActivity(intent);
                    }
                }, 2000);
            }
        };
    }

    private String getFileName(Uri uri) {
        String result = "";
        Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (cursor.moveToFirst() && nameIndex != -1) {
                result = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return result;
    }

    private Runnable showErrorsStudent(Map<String, String> errors) {
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
                        case "category":
                            binding.errorCategory.setVisibility(View.VISIBLE);
                            binding.errorCategory.setText(error);
                            break;
                        case "summary":
                            binding.inputSummaryStudent.getError().setVisibility(View.VISIBLE);
                            binding.inputSummaryStudent.getError().setText(error);
                            break;
                    }
                }
            }
        };
    }

    private Runnable showErrorsInstructor(Map<String, String> errors) {
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

                    if (field.equals("summary")) {
                        binding.inputSummaryInstructor.getError().setVisibility(View.VISIBLE);
                        binding.inputSummaryInstructor.getError().setText(error);
                    }

                    if (field.equals("resume")) {
                        binding.errorResume.setVisibility(View.VISIBLE);
                        binding.errorResume.setText(error);
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.errorCategory.setText(null);
        binding.errorCategory.setVisibility(View.GONE);
        binding.inputSummaryStudent.getError().setText(null);
        binding.inputSummaryStudent.getError().setVisibility(View.GONE);
        binding.inputSummaryInstructor.getError().setText(null);
        binding.inputSummaryInstructor.getError().setVisibility(View.GONE);
        binding.errorResume.setText(null);
        binding.errorResume.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        inputResume = null;
        instructorResume = null;
        formStudent = null;
        formInstructor = null;
        buttonFormStudent = null;
        buttonFormInstructor = null;
    }
}