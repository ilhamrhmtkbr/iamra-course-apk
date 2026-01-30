package com.ilhamrhmtkbr.presentation.student.certificates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.common.Certificate;

import java.util.ArrayList;
import java.util.List;

public class CertificatesAdapter extends RecyclerView.Adapter<CertificatesAdapter.CertificatesViewHolder> {
    private final List<Certificate> data = new ArrayList<>();

    public interface onClickListener {
        void detail(String certificateId);
    }

    private onClickListener onClickListener;

    public CertificatesAdapter(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setData(List<Certificate> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CertificatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_certificate, parent, false);
        return new CertificatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificatesViewHolder holder, int position) {
        Certificate certificate = data.get(position);
        holder.course_title.setText(certificate.getInstructorCourseTitle());
        holder.created_at.setText(certificate.getCreatedAt());
        holder.student_name.setText(certificate.getStudentName());
        holder.button_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.detail(certificate.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CertificatesViewHolder extends RecyclerView.ViewHolder {
        TextView student_name, course_title, created_at, button_detail;

        public CertificatesViewHolder(@NonNull View itemView) {
            super(itemView);

            student_name = itemView.findViewById(R.id.certificate_student);
            course_title = itemView.findViewById(R.id.certificate_title);
            created_at = itemView.findViewById(R.id.certificate_at);
            button_detail = itemView.findViewById(R.id.button_detail);
        }
    }
}
