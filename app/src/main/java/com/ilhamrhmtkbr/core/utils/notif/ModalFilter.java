package com.ilhamrhmtkbr.core.utils.notif;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;

import java.util.List;

public class ModalFilter {
    public interface OnSortOptionSelectedListener {
        void onSortOptionSelected(String option);
    }


    public static class SortOptionAdapter extends RecyclerView.Adapter<SortOptionAdapter.ViewHolder>{
        private final List<String> options;
        private final OnSortOptionSelectedListener listener;

        public SortOptionAdapter(List<String> options, OnSortOptionSelectedListener onSortOptionSelectedListener){
            this.options = options;
            this.listener = onSortOptionSelectedListener;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            TextView option;

            public ViewHolder(View view){
                super(view);
                option = view.findViewById(R.id.option);
            }
        }

        @NonNull
        @Override
        public SortOptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.component_modal_filter_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SortOptionAdapter.ViewHolder holder, int position) {
            String option = options.get(position);
            holder.option.setText(option);
            holder.itemView.setOnClickListener(v -> {
                listener.onSortOptionSelected(option);
            });
        }

        @Override
        public int getItemCount() {
            return options.size(); // <-- ini yang bener
        }
    }
}
