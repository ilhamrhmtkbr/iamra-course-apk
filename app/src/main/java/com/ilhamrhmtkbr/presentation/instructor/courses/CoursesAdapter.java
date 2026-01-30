package com.ilhamrhmtkbr.presentation.instructor.courses;

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

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.InstructorCoursesViewHolder> {
    private List<Course> data = new ArrayList<>();

    private onClickListener listener;

    public interface onClickListener {
        void addCoupon(String courseId);

        void section(String courseId, String courseTitle, String courseEditor);

        void edit(String courseId);
    }

    public CoursesAdapter(onClickListener onClickListener) {
        listener = onClickListener;
    }

    public void setData(List<Course> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructorCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instructor_course, parent, false);
        return new InstructorCoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorCoursesViewHolder holder, int position) {
        Course courseItem = data.get(position);

        Glide.with(holder.courseImage.getContext())
                .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + courseItem.getImage())
                .centerCrop()
                .into(holder.courseImage);

        holder.courseTitle.setText(courseItem.getTitle());
        holder.courseDesc.setText(courseItem.getDescription());

        holder.buttonAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addCoupon(courseItem.getId());
            }
        });

        holder.buttonSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.section(courseItem.getId(), courseItem.getTitle(), courseItem.getEditor());
            }
        });

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.edit(courseItem.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class InstructorCoursesViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView courseTitle, courseDesc, buttonAddCoupon, buttonSection, buttonEdit;

        public InstructorCoursesViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImage = itemView.findViewById(R.id.courseImage);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            courseDesc = itemView.findViewById(R.id.courseDesc);
            buttonAddCoupon = itemView.findViewById(R.id.button_add_coupon);
            buttonSection = itemView.findViewById(R.id.button_section);
            buttonEdit = itemView.findViewById(R.id.button_edit);
        }
    }
}
