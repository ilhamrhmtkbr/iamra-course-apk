package com.ilhamrhmtkbr.presentation.utils.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ilhamrhmtkbr.R;

public class ButtonSubmit extends ConstraintLayout {
    private TextView btnText;

    public ButtonSubmit(Context context) {
        super(context);
        init(context, null);
    }

    public ButtonSubmit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ButtonSubmit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.component_button, this, true);
        btnText = findViewById(R.id.btnText);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
            String text = a.getString(R.styleable.CustomButton_custom_text);
            if (text != null) {
                setText(text);
            }
            a.recycle();
        }
    }

    public void setText(String text) {
        btnText.setText(text);
    }

    public String getText() {
        return btnText.getText().toString();
    }
}
