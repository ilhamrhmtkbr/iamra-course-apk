package com.ilhamrhmtkbr.presentation.utils.tools;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

public class FormStateHandler<T> {
    public void handle(FormState<T> stringFormState, ConstraintLayout loading, Runnable showError, Runnable afterSuccess) {
        switch (stringFormState.getStatus()) {
            case IDLE:
                break;
            case LOADING:
                LoadingUtil.setup(true, loading);
                break;
            case SUCCESS:
                LoadingUtil.setup(false, loading);
                DialogUtil.showSuccessSnackbar(loading.getRootView(), stringFormState.getMessage());
                if (afterSuccess != null) {
                    afterSuccess.run();
                }
                break;
            case ERROR:
                LoadingUtil.setup(false, loading);
                DialogUtil.showErrorSnackbar(loading.getRootView(), stringFormState.getMessage() != null ? stringFormState.getMessage() : "error");
                break;
            case VALIDATION_ERROR:
                LoadingUtil.setup(false, loading);
                DialogUtil.showErrorSnackbar(loading.getRootView(), stringFormState.getMessage() != null ? stringFormState.getMessage() : "validation error");
                if (stringFormState.getValidationErrors() == null) return;
                showError.run();
                break;
        }
    }
}
