package com.ilhamrhmtkbr.presentation.instructor.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.utils.component.DataInfo;
import com.ilhamrhmtkbr.domain.model.instructor.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.InstructorReviewsViewHolder> {
    private final List<Review> data = new ArrayList<>();

    public void setData(List<Review> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructorReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instructor_review, parent, false);
        return new InstructorReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorReviewsViewHolder holder, int position) {
        Review item = data.get(position);
        holder.studentName.setValue(item.getStudentFullName());
        holder.courseTitle.setValue(item.getCourseTitle());
        holder.review.setValue(item.getStudentReview());
        holder.rating.setRating(Math.round(Integer.parseInt(item.getStudentRating()) / 2));
        holder.createdAt.setText(item.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class InstructorReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView createdAt;
        RatingBar rating;
        DataInfo studentName, courseTitle, review;

        public InstructorReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name);
            courseTitle = itemView.findViewById(R.id.course_title);
            review = itemView.findViewById(R.id.review);
            rating = itemView.findViewById(R.id.rating);
            createdAt = itemView.findViewById(R.id.created_at);
        }
    }
}
