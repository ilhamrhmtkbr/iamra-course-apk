package com.ilhamrhmtkbr.presentation.student.carts;

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
import com.ilhamrhmtkbr.domain.model.student.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<Cart> data = new ArrayList<>();
    private final onClickListeners onClickListeners;

    public interface onClickListeners {
        void buy(String courseId, String courseImage, String courseTitle, String courseDesc,
                 String coursePrice, String courseLevel, String courseStatus);

        void delete(String cartId);
    }

    public CartAdapter(onClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public void setData(List<Cart> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_cart, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cartItem = data.get(position);

        Glide.with(holder.courseImage.getContext())
                .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + cartItem.getInstructorCourse().getImage())
                .centerCrop()
                .into(holder.courseImage);

        holder.courseTitle.setText(cartItem.getInstructorCourse().getTitle());
        holder.courseDesc.setText(cartItem.getInstructorCourse().getDescription());

        holder.buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.buy(cartItem.getInstructorCourseId(),
                            cartItem.getInstructorCourse().getImage(),
                            cartItem.getInstructorCourse().getTitle(),
                            cartItem.getInstructorCourse().getDescription(),
                            cartItem.getInstructorCourse().getPrice(),
                            cartItem.getInstructorCourse().getLevel(),
                            cartItem.getInstructorCourse().getStatus());
                }
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.delete(cartItem.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView courseTitle, courseDesc, buttonDelete, buttonBuy;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImage = itemView.findViewById(R.id.courseImage);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            courseDesc = itemView.findViewById(R.id.courseDesc);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonBuy = itemView.findViewById(R.id.buttonBuy);
        }
    }
}
