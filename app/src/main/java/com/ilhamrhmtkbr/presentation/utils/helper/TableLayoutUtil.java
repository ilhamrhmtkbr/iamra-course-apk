package com.ilhamrhmtkbr.presentation.utils.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.ilhamrhmtkbr.R;

public class TableLayoutUtil {
    public static TableRow setup(Context context, String[] headersText) {
        TableRow headerRow = new TableRow(context);
        for (int i = 0; i < headersText.length; i++) {
            TextView headerView = new TextView(context);
            headerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            headerView.setText(headersText[i]);
            headerView.setPadding(
                    context.getResources().getDimensionPixelSize(R.dimen.size_m),
                    context.getResources().getDimensionPixelSize(R.dimen.size_m),
                    context.getResources().getDimensionPixelSize(R.dimen.size_m),
                    context.getResources().getDimensionPixelSize(R.dimen.size_m)
            );
            headerView.setTextColor(context.getResources().getColor(R.color.second_bg_color));

            if (i == 0) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                headerView.setLayoutParams(params);
                headerView.setBackgroundResource(R.drawable.table_cell_item_header_left);
            } else if (i == headersText.length - 1) {
                headerView.setBackgroundResource(R.drawable.table_cell_item_header_right);
            } else {
                headerView.setBackgroundColor(context.getResources().getColor(R.color.text_color));
            }

            headerView.setTypeface(null, Typeface.BOLD);
            headerRow.addView(headerView);
        }

        return headerRow;
    }
}
