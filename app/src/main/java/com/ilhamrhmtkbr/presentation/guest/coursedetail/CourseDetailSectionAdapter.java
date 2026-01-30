package com.ilhamrhmtkbr.presentation.guest.coursedetail;

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

public class CourseDetailSectionAdapter extends RecyclerView.Adapter<CourseDetailSectionAdapter.CourseDetailSectionViewHolder> {
    private List<Section> sectionsList = new ArrayList<>();

    // Method untuk set data dari luar
    public void setSectionsList(List<Section> sections) {
        this.sectionsList = sections != null ? sections : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseDetailSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bottom_sheet_public_course_section, parent, false);
        return new CourseDetailSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseDetailSectionViewHolder holder, int position) {
        Section section = sectionsList.get(position);
        holder.sectionName.setText(section.getTitle());
    }

    @Override
    public int getItemCount() {
        return sectionsList.size();
    }

    public static class CourseDetailSectionViewHolder extends RecyclerView.ViewHolder {
        public TextView sectionName;

        public CourseDetailSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_name);
        }
    }
}