package com.ilhamrhmtkbr.presentation.utils.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ilhamrhmtkbr.R;

public class DataInfo extends ConstraintLayout {
    private TextView tv_value;

    public DataInfo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public DataInfo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.component_info, this, true);

        TextView title = findViewById(R.id.tv_title);
        tv_value = findViewById(R.id.tv_value);

        if (attributeSet != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomInfo);
            String customTitle = a.getString(R.styleable.CustomInfo_custom_title);
            if (customTitle != null) {
                title.setText(customTitle);
            }

            a.recycle();
        }
    }

    public void setValue(String value) {
        this.tv_value.setText(value);
    }
}
