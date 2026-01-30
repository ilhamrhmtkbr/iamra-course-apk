package com.ilhamrhmtkbr.presentation.forum.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.forum.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ChatViewHolder> {
    private List<Group> chatData;
    private OnGroupClickListener clickListener;

    public interface OnGroupClickListener {
        void onGroupClick(Group group);
    }

    public GroupAdapter(List<Group> chatData, OnGroupClickListener listener) {
        this.chatData = chatData;
        this.clickListener = listener;
    }

    public void updateData(List<Group> newData) {
        this.chatData = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_chat_group, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Group data = chatData.get(position);
        holder.bind(data, clickListener);
    }

    @Override
    public int getItemCount() {
        return chatData != null ? chatData.size() : 0;
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatTitle;

        public ChatViewHolder(@NonNull View item) {
            super(item);
            chatTitle = item.findViewById(R.id.chat_title);
        }

        public void bind(Group chat, OnGroupClickListener listener) {
            chatTitle.setText(chat.getTitle());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onGroupClick(chat);
                }
            });
        }
    }
}