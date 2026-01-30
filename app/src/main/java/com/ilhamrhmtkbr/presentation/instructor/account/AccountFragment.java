package com.ilhamrhmtkbr.presentation.instructor.account;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.InputType;
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
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.domain.model.instructor.Account;
import com.ilhamrhmtkbr.databinding.FragmentInstructorAccountBinding;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment implements SetupBanks {
    private FragmentInstructorAccountBinding binding;
    private AccountViewModels viewModel;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(AccountViewModels.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorAccountBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setupElement();
        setupInputBanks(binding.inputBank, getLayoutInflater(), requireContext());
        observeViewModelGetData();
        observeViewModelValidationError();
    }

    private void setupElement() {
        binding.inputAccount.getInput().setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void observeViewModelGetData() {
        viewModel.getAccount().observe(getViewLifecycleOwner(), new Observer<Resource<Account>>() {
            @Override
            public void onChanged(Resource<Account> accountResource) {
                LoadingUtil.setup(accountResource.isLoading(), binding.loading);
                if (!accountResource.isLoading() && accountResource.getData() != null) {
                    Account account = accountResource.getData();
                    binding.inputAccount.setValue(account.getAccountId());
                    binding.inputAlias.setValue(account.getAliasName());
                    binding.inputBank.setText(account.getBankName());

                    binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.modify(
                                    binding.inputAccount.getValue(),
                                    binding.inputAlias.getValue(),
                                    binding.inputBank.getText().toString()
                            );
                        }
                    });
                } else if (!accountResource.isLoading() && accountResource.getData() == null) {
                    binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.store(
                                    binding.inputAccount.getValue(),
                                    binding.inputAlias.getValue(),
                                    binding.inputBank.getText().toString()
                            );
                        }
                    });
                }
            }
        });
    }

    private void observeViewModelValidationError() {
        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showError(stringStringMap).run();
            }
        });

        viewModel.getAccountFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> instructorAccountResponseFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(instructorAccountResponseFormState, binding.loading, showError(instructorAccountResponseFormState.getValidationErrors()), afterSuccess());
            }
        });
    }

    private Runnable showError(Map<String, String> errors) {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                if (errors.isEmpty()) {
                    return;
                }
                DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));

                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    String field = entry.getKey();
                    String error = entry.getValue();
                    switch (field) {
                        case "account_id":
                            binding.inputAccount.getError().setVisibility(VISIBLE);
                            binding.inputAccount.getError().setText(error);
                            break;
                        case "bank_name":
                            binding.errorBank.setVisibility(VISIBLE);
                            binding.errorBank.setText(error);
                            break;
                        case "alias_name":
                            binding.inputAlias.getError().setVisibility(VISIBLE);
                            binding.inputAlias.getError().setText(error);
                            break;
                        default:
                            Toast.makeText(requireContext(), field + ": " + error, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        };
    }

    private Runnable afterSuccess() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshAccount();
            }
        };
    }

    private void removeErrors() {
        binding.inputAccount.getError().setText(null);
        binding.inputAccount.getError().setVisibility(GONE);
        binding.inputAlias.getError().setText(null);
        binding.inputAlias.getError().setVisibility(GONE);
        binding.errorBank.setText(null);
        binding.errorBank.setVisibility(GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
