package com.ilhamrhmtkbr.presentation.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;

import java.util.List;

public class AccordionAdapter extends RecyclerView.Adapter<AccordionAdapter.AccordionViewHolder> {
    private final List<AccordionItem> accordionItemList;

    public AccordionAdapter(List<AccordionItem> items) {
        this.accordionItemList = items;
    }

    @NonNull
    @Override
    public AccordionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_accordion_item, parent, false);
        return new AccordionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccordionViewHolder viewHolder, int position) {
        // INI YANG KURANG - harus panggil bind()
        viewHolder.bind(accordionItemList.get(position));
    }

    @Override
    public int getItemCount(){
        return accordionItemList.size();
    }

    public static class AccordionViewHolder extends RecyclerView.ViewHolder{
        TextView accordionTitle, accordionContent;
        ImageView accordionIcon;
        ConstraintLayout accordion;

        public AccordionViewHolder(@NonNull View itemView) {
            super(itemView);

            accordion = itemView.findViewById(R.id.accordion);
            accordionTitle = itemView.findViewById(R.id.accordion_title);
            accordionContent = itemView.findViewById(R.id.accordion_content);
            accordionIcon = itemView.findViewById(R.id.accordion_icon);
        }

        void bind(AccordionItem item) {
            accordionTitle.setText(item.title);
            accordionContent.setText(item.content);

            // Set initial state
            accordionContent.setVisibility(item.isExpanded ? View.VISIBLE : View.GONE);
            accordionIcon.setRotation(item.isExpanded ? 90 : 0);

            accordion.setOnClickListener(v -> {
                item.isExpanded = !item.isExpanded;

                if (item.isExpanded) {
                    expand(accordionContent);
                    accordionIcon.animate().rotation(90).setDuration(300).start();
                } else {
                    collapse(accordionContent);
                    accordionIcon.animate().rotation(0).setDuration(300).start();
                }
            });
        }

        private void expand(final View v) {
            v.measure(
                    View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            final int targetHeight = v.getMeasuredHeight();

            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                            ? ViewGroup.LayoutParams.WRAP_CONTENT
                            : (int) (targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }

        private void collapse(final View v) {
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                    } else {
                        v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }
    }

    public static class AccordionItem{
        String title;
        String content;
        boolean isExpanded;

        public AccordionItem(String title, String content) {
            this.title = title;
            this.content = content;
            this.isExpanded = false;
        }
    }
}