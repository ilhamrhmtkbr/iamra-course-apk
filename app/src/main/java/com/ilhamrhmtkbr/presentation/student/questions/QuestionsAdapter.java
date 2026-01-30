package com.ilhamrhmtkbr.presentation.student.questions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.utils.component.DataInfo;
import com.ilhamrhmtkbr.domain.model.student.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {
    private final List<Question> data = new ArrayList<>();
    private final onClickListeners onClickListeners;

    public QuestionsAdapter(onClickListeners onClickListeners){
        this.onClickListeners = onClickListeners;
    }

    public interface onClickListeners{
        void detail(String questionId, String courseId);
    }

    public void setData(List<Question> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = data.get(position);
        holder.courseId.setValue(question.getInstructorCourseId());
        holder.courseTitle.setValue(question.getCourseTitle());
        holder.question.setValue(question.getQuestion());
        holder.createdAt.setText(question.getCreatedAt());
        holder.buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListeners.detail(question.getId(), question.getInstructorCourseId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView createdAt, buttonDetail;
        DataInfo courseId, courseTitle, question;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            courseId = itemView.findViewById(R.id.course_id);
            courseTitle = itemView.findViewById(R.id.course_title);
            question = itemView.findViewById(R.id.question);
            createdAt = itemView.findViewById(R.id.created_at);
            buttonDetail = itemView.findViewById(R.id.button_detail);
        }
    }
}
