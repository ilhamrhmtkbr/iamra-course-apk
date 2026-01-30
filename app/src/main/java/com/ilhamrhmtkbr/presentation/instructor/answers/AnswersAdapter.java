package com.ilhamrhmtkbr.presentation.instructor.answers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.instructor.Answer;
import com.ilhamrhmtkbr.presentation.utils.component.DataInfo;

import java.util.ArrayList;
import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.InstructorAnswersViewHolder> {
    private final List<Answer> data = new ArrayList<>();
    private final onClickListener listener;

    public interface onClickListener {
        void edit(Answer data);
    }

    public AnswersAdapter(onClickListener onClickListener) {
        listener = onClickListener;
    }

    public void setData(List<Answer> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructorAnswersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instructor_answer, parent, false);
        return new InstructorAnswersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorAnswersViewHolder holder, int position) {
        Answer item = data.get(position);
        holder.studentName.setValue(item.getStudent());
        holder.courseTitle.setValue(item.getTitle());
        holder.question.setValue(item.getQuestion());
        holder.answer.setValue(item.getAnswer());
        holder.createdAt.setText(item.getQuestionCreatedAt());

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.edit(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class InstructorAnswersViewHolder extends RecyclerView.ViewHolder {
        TextView createdAt, buttonEdit;
        DataInfo studentName, courseTitle, question, answer;

        public InstructorAnswersViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name);
            courseTitle = itemView.findViewById(R.id.course_title);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            createdAt = itemView.findViewById(R.id.created_at);
            buttonEdit = itemView.findViewById(R.id.button_edit);
        }
    }
}
