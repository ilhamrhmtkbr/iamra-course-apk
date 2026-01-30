package com.ilhamrhmtkbr.presentation.utils.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;

public class ScrollIndicator extends View {
    private Paint trackPaint;
    private Paint thumbPaint;
    private float scrollProgress = 0f;
    private int thumbWidth = 17; // lebar indicator (dp akan dikonversi)
    private int indicatorHeight = 3; // tinggi indicator
    private int horizontalPadding = 145; // padding kiri-kanan

    public ScrollIndicator(Context context) {
        super(context);
        init(context);
    }

    public ScrollIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Convert dp to px
        float density = context.getResources().getDisplayMetrics().density;
        thumbWidth = (int) (thumbWidth * density);
        indicatorHeight = (int) (indicatorHeight * density);
        horizontalPadding = (int) (horizontalPadding * density);

        // Track paint (background abu-abu)
        trackPaint = new Paint();
        trackPaint.setColor(ContextCompat.getColor(context, R.color.border_color)); // atau #E0E0E0
        trackPaint.setAntiAlias(true);

        // Thumb paint (indicator biru/merah)
        thumbPaint = new Paint();
        thumbPaint.setColor(ContextCompat.getColor(context, R.color.blue_color)); // atau #EE4D2D untuk merah
        thumbPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float centerY = height / 2f;

        // Draw track (background)
        float trackLeft = horizontalPadding;
        float trackRight = width - horizontalPadding;
        canvas.drawRoundRect(
                trackLeft,
                centerY - indicatorHeight / 2f,
                trackRight,
                centerY + indicatorHeight / 2f,
                indicatorHeight / 2f,
                indicatorHeight / 2f,
                trackPaint
        );

        // Draw thumb (indicator yang bergerak)
        float availableWidth = trackRight - trackLeft - thumbWidth;
        float thumbLeft = trackLeft + (scrollProgress * availableWidth);
        canvas.drawRoundRect(
                thumbLeft,
                centerY - indicatorHeight / 2f,
                thumbLeft + thumbWidth,
                centerY + indicatorHeight / 2f,
                indicatorHeight / 2f,
                indicatorHeight / 2f,
                thumbPaint
        );
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateProgress(recyclerView);
            }
        });
    }

    private void updateProgress(RecyclerView recyclerView) {
        int scrollRange = recyclerView.computeHorizontalScrollRange();
        int scrollExtent = recyclerView.computeHorizontalScrollExtent();
        int scrollOffset = recyclerView.computeHorizontalScrollOffset();

        if (scrollRange > scrollExtent) {
            scrollProgress = (float) scrollOffset / (scrollRange - scrollExtent);
            scrollProgress = Math.max(0f, Math.min(1f, scrollProgress));
            invalidate(); // redraw
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = indicatorHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}