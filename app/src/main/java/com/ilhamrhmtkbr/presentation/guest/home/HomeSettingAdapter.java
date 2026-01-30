package com.ilhamrhmtkbr.presentation.guest.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;

import java.util.ArrayList;
import java.util.List;

public class HomeSettingAdapter extends RecyclerView.Adapter<HomeSettingAdapter.HomeSettingViewHolder> {
    private final List<HomeSettingItem> settingItems = new ArrayList<>();
    private final OnclickListener onclickListener;

    public interface OnclickListener{
        void run(String title);
    }
    public HomeSettingAdapter(List<HomeSettingItem> settingItemList, OnclickListener onclickListener) {
        this.settingItems.addAll(settingItemList);
        this.onclickListener = onclickListener;
    }

    @NonNull
    @Override
    public HomeSettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_public_home_setting, parent, false);
        return new HomeSettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSettingViewHolder holder, int position) {
        HomeSettingItem settingItem = settingItems.get(position);
        holder.title.setText(settingItem.title);
        holder.icon.setImageResource(settingItem.icon);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickListener.run(settingItem.title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }

    public static class HomeSettingViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView icon;
        public ConstraintLayout button;

        public HomeSettingViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.setting_title);
            icon = itemView.findViewById(R.id.setting_icon);
            button = itemView.findViewById(R.id.setting_button);
        }
    }

    public static class HomeSettingItem{
        private final String title;
        private final int icon;

        public HomeSettingItem(String title, int icon){
            this.title = title;
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public int getIcon() {
            return icon;
        }
    }
}
