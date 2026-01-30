package com.ilhamrhmtkbr.presentation.student.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.utils.component.DataInfo;
import com.ilhamrhmtkbr.domain.model.student.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private final List<Review> data = new ArrayList<>();
    private final onClickListener onClickListener;

    public void setData(List<Review> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public interface onClickListener {
        void edit(String reviewId, String courseId, String courseTitle, String courseDesc, String review, String rating);
    }

    public ReviewsAdapter(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_review, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        Review review = data.get(position);
        holder.courseTitle.setValue(review.getInstructorCourse().getTitle());
        holder.courseDesc.setValue(review.getInstructorCourse().getDescription());
        holder.review.setValue(review.getReview());
        holder.rating.setRating(Math.round(Integer.parseInt(review.getRating()) / 2));
        holder.created_at.setText(review.getCreated_at());
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.edit(
                        review.getId(),
                        review.getInstructorCourse().getId(),
                        review.getInstructorCourse().getTitle(),
                        review.getInstructorCourse().getDescription(),
                        review.getReview(),
                        review.getRating());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView created_at, buttonEdit;
        RatingBar rating;
        DataInfo courseTitle, courseDesc, review;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            courseTitle = itemView.findViewById(R.id.course_title);
            courseDesc = itemView.findViewById(R.id.course_desc);
            review = itemView.findViewById(R.id.course_review);
            rating = itemView.findViewById(R.id.course_rating);
            created_at = itemView.findViewById(R.id.created_at);
            buttonEdit = itemView.findViewById(R.id.button_edit);
        }
    }
}
