package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class StudentTransactionResponse {
    @SerializedName("data")
    public List<TransactionItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class TransactionItem {
        @SerializedName("order_id")
        public String order_id;

        @SerializedName("amount")
        public String amount;

        @SerializedName("midtrans_data")
        public String midtrans_data;

        @SerializedName("status")
        public String status;

        @SerializedName("created_at")
        public String created_at;
    }
}