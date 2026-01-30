package com.ilhamrhmtkbr.presentation.utils.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<PaginationAdapter.ViewHolder> {
    private final List<Page> links = new ArrayList<>();
    private final OnPageClickListener listener;

    public interface OnPageClickListener {
        void onPageClick(String url);
    }

    public PaginationAdapter(OnPageClickListener listener){
        this.listener = listener;
    }

    public void updateLinks(List<Page> newLinks){
        links.clear();
        if (newLinks != null) {
            links.addAll(newLinks);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView btn;

        public ViewHolder(View item){
            super(item);
            btn = item.findViewById(R.id.btn_page);
        }
    }

    @NonNull
    @Override
    public PaginationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_pagination_page_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaginationAdapter.ViewHolder holder, int position){
        Page link = links.get(position);

        String cleanLabel = link.getLabel();
        if (link.getLabel().contains("&laquo;")){
            cleanLabel = "<";
        } else if (link.getLabel().contains("&raquo;")){
            cleanLabel = ">";
        }

        holder.btn.setText(cleanLabel);

        holder.btn.setEnabled(link.getUrl() != null);

        Drawable bg;
        if (link.getActive()) {
            bg = ContextCompat.getDrawable(holder.btn.getContext(), R.drawable.bg_blue_radiuss);
            holder.btn.setTextColor(Color.WHITE);
        } else {
            bg = ContextCompat.getDrawable(holder.btn.getContext(), R.drawable.bg_themefirst_radiuss_bordercolordefault);
            holder.btn.setTextColor(
                    ContextCompat.getColor(holder.btn.getContext(), R.color.text_color)
            );
        }
        holder.btn.setBackground(bg);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link.getUrl() != null) listener.onPageClick(link.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
}
