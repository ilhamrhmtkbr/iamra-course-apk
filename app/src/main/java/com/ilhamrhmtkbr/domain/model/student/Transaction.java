package com.ilhamrhmtkbr.domain.model.student;

public class Transaction {
    private final String order_id;
    private final String amount;
    private final String midtransData;
    private final String status;
    private final String created_at;

    public Transaction(String order_id, String amount, String midtransData, String status, String created_at) {
        this.order_id = order_id;
        this.amount = amount;
        this.midtransData = midtransData;
        this.status = status;
        this.created_at = created_at;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getAmount() {
        return amount;
    }

    public String getMidtransData() {
        return midtransData;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }
}