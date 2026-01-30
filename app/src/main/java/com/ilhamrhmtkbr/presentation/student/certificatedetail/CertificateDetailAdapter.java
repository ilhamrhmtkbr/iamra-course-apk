package com.ilhamrhmtkbr.presentation.student.certificatedetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.common.Section;

import java.util.ArrayList;
import java.util.List;

public class CertificateDetailAdapter extends RecyclerView.Adapter<CertificateDetailAdapter.CertificateDetailViewHolder> {
    private final List<Section> data = new ArrayList<>();

    public void setData(List<Section> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CertificateDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_certificate_detail, parent, false);
        return new CertificateDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateDetailViewHolder holder, int position) {
        String no = String.valueOf(position + 1);
        holder.no.setText(no);
        holder.title.setText(this.data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CertificateDetailViewHolder extends RecyclerView.ViewHolder{
        TextView no, title;

        public CertificateDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.no);
            title = itemView.findViewById(R.id.title);
        }
    }
}
