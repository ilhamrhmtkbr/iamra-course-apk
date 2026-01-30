package com.ilhamrhmtkbr.core.base;

import android.util.Patterns;

import com.ilhamrhmtkbr.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseValidation {
    private String value;
    private String error;
    private String fieldName;

    // ✅ No Context parameter needed!
    public BaseValidation validation(String fieldName, String value) {
        this.error = null;
        this.fieldName = fieldName;
        this.value = value;
        return this;
    }

    public BaseValidation required() {
        if (value == null || value.trim().isEmpty()) {
            // ✅ Langsung pake ValidationHelper.getString()
            error = ValidationHelper.getString(R.string.base_validation_field_required, fieldName);
        }
        return this;
    }

    public BaseValidation email() {
        if (!Patterns.EMAIL_ADDRESS.matcher(this.value.trim()).matches()) {
            error = ValidationHelper.getString(R.string.base_validation_field_email, fieldName);
        }
        return this;
    }

    public BaseValidation passwordCompleted() {
        if (!this.value.matches(".*[a-z].*")) {
            error = ValidationHelper.getString(R.string.base_validation_password_lowercase, fieldName);
            return this;
        }
        if (!this.value.matches(".*[A-Z].*")) {
            error = ValidationHelper.getString(R.string.base_validation_password_uppercase, fieldName);
            return this;
        }
        if (!this.value.matches(".*[0-9].*")) {
            error = ValidationHelper.getString(R.string.base_validation_password_number, fieldName);
            return this;
        }
        if (!this.value.matches(".*[^a-zA-Z0-9].*")) {
            error = ValidationHelper.getString(R.string.base_validation_password_symbol, fieldName);
            return this;
        }
        return this;
    }

    public BaseValidation passwordConfirm(String passwordConfirm) {
        if (!this.value.equals(passwordConfirm)) {
            error = ValidationHelper.getString(R.string.base_validation_password_not_match, fieldName);
        }
        return this;
    }

    public BaseValidation minMax(int min, int max) {
        int length = value.trim().length();
        if (length < min || length > max) {
            error = ValidationHelper.getString(R.string.base_validation_field_min_max, fieldName, min, max);
        }
        return this;
    }

    public BaseValidation phoneId() {
        if (!this.value.matches("^08[0-9]{9,11}$")) {
            error = ValidationHelper.getString(R.string.base_validation_phone_invalid);
        }
        return this;
    }

    public BaseValidation dob() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(this.value);
            if (date.after(new Date())) {
                error = ValidationHelper.getString(R.string.base_validation_dob_future);
            }
        } catch (ParseException e) {
            error = ValidationHelper.getString(R.string.base_validation_dob_invalid);
        }
        return this;
    }

    public boolean hasError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public String getFieldName() {
        return fieldName;
    }
}