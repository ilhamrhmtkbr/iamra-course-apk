package com.ilhamrhmtkbr.presentation.utils.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ilhamrhmtkbr.R;

public class InputText extends ConstraintLayout {
    private TextView error;
    private EditText input;

    public InputText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InputText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.component_input_text, this, true); // Cannot resolve method 'inflate(int, InputText, boolean)'

        TextView label = findViewById(R.id.label);
        input = findViewById(R.id.input);
        error = findViewById(R.id.error);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomInput);
            String customLabel = a.getString(R.styleable.CustomInput_custom_label);
            if (customLabel != null) {
                label.setText(customLabel);
            }

            int customType = a.getInteger(R.styleable.CustomInput_custom_type, -1);
            if (customType != -1) {
                input.setInputType(customType);
            }
            a.recycle();
        }
    }

    public String getValue() {
        return input.getText().toString().trim();
    }

    public void setValue(String value) {
        this.input.setText(value);
    }

    public void setReadOnly(boolean isEnable) {
        this.input.setEnabled(isEnable);
    }

    public TextView getError() {
        return error;
    }

    public EditText getInput() {
        return input;
    }
}
