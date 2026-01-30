package com.ilhamrhmtkbr.presentation.student.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.common.Course;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder> {
    private final List<Course> data = new ArrayList<>();

    public interface onClickListeners {
        void review(String courseId);

        void question(String courseId);

        void detail(String courseId);
    }

    private final onClickListeners onClickListeners;

    public CoursesAdapter(onClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public void setData(List<Course> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_course, parent, false);

        return new CoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        Course course = data.get(position);
        holder.course_title.setText(course.getTitle());
        holder.course_desc.setText(course.getDescription());
        Glide.with(holder.course_image.getContext())
                .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + course.getImage())
                .centerCrop()
                .into(holder.course_image);
        holder.button_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListeners.review(course.getId());
            }
        });

        holder.button_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListeners.question(course.getId());
            }
        });

        holder.button_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListeners.detail(course.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CoursesViewHolder extends RecyclerView.ViewHolder {
        ImageView course_image;
        TextView course_title, course_desc, button_review, button_question, button_detail;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);

            course_image = itemView.findViewById(R.id.course_image);
            course_title = itemView.findViewById(R.id.course_title);
            course_desc = itemView.findViewById(R.id.course_desc);
            button_review = itemView.findViewById(R.id.button_review);
            button_question = itemView.findViewById(R.id.button_question);
            button_detail = itemView.findViewById(R.id.button_detail);
        }
    }
}
