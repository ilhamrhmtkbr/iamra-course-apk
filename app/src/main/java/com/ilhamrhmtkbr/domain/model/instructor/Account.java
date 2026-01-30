package com.ilhamrhmtkbr.domain.model.instructor;

public class Account {
    private final String accountId;
    private final String bankName;
    private final String aliasName;

    public Account(String accountId, String bankName, String aliasName) {
        this.accountId = accountId;
        this.bankName = bankName;
        this.aliasName = aliasName;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAliasName() {
        return aliasName;
    }
}