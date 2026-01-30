package com.ilhamrhmtkbr.presentation.guest.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.common.Course;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {
    private List<Course> courseList = new ArrayList<>();
    public interface OnCourseClickListener {
        void onCourseClick(String courseId);
    }

    private OnCourseClickListener listener;

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;
    }

    public void setCourseList(List<Course> courses) {
        this.courseList = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_public_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        Glide.with(holder.courseImage.getContext())
                .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + course.getImage())
                .centerCrop()
                .into(holder.courseImage);

        holder.courseTitle.setText(course.getTitle());
        holder.courseInstructor.setText(course.getInstructorId());
        holder.courseTag.setText(course.getEditor());
        holder.coursePrice.setText(course.getPrice());
        holder.cardCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCourseClick(course.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView courseTitle, courseInstructor,coursePrice, courseTag;
        ConstraintLayout cardCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.course_image);
            courseTitle = itemView.findViewById(R.id.course_title);
            courseInstructor = itemView.findViewById(R.id.course_instructor);
            coursePrice = itemView.findViewById(R.id.course_price);
            courseTag = itemView.findViewById(R.id.course_tag);
            cardCourse = itemView.findViewById(R.id.card_course);
        }
    }
}
