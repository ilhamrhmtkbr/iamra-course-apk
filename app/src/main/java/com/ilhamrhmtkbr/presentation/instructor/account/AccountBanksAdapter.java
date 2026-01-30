package com.ilhamrhmtkbr.presentation.instructor.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;

import java.util.List;

public class AccountBanksAdapter extends RecyclerView.Adapter<AccountBanksAdapter.InstructorAccountBanksViewHolder> {
    private List<BankItem> list;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(BankItem item);
    }

    public AccountBanksAdapter(List<BankItem> newList, OnItemClickListener onItemClickListener) {
        list = newList;
        listener = onItemClickListener;
    }

    public void updateList(List<BankItem> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructorAccountBanksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instructor_account_bank, parent, false);
        return new InstructorAccountBanksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorAccountBanksViewHolder holder, int position) {
        BankItem bankItem = list.get(position);
        holder.itemBankName.setText(bankItem.name);

        holder.itemBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(bankItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class InstructorAccountBanksViewHolder extends RecyclerView.ViewHolder{
        public TextView itemBankName;

        public InstructorAccountBanksViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBankName = itemView.findViewById(R.id.bank_name);
        }
    }

    public static class BankItem {
        public String key;
        public String name;

        public BankItem(String key, String name) {
            this.key = key;
            this.name = name;
        }
    }

}
