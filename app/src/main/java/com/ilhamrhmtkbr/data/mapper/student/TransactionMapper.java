package com.ilhamrhmtkbr.data.mapper.student;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionResponse;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionMapper {
    public static List<StudentTransactionsEntity> fromResponseToEntities(StudentTransactionResponse response) {
        List<StudentTransactionsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (StudentTransactionResponse.TransactionItem item : response.data) {
                StudentTransactionsEntity transactionEntity = new StudentTransactionsEntity();
                transactionEntity.order_id = item.order_id;
                transactionEntity.amount = item.amount;
                transactionEntity.midtrans_data = item.midtrans_data;
                transactionEntity.status = item.status;
                transactionEntity.created_at = item.created_at;
                newFormat.add(transactionEntity);
            }
        }

        return newFormat;
    }

    public static List<StudentTransactionsPaginationEntity> fromResponseToPaginationEntities(StudentTransactionResponse response) {
        List<StudentTransactionsPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.links != null) {
            for (Page item : response.links) {
                StudentTransactionsPaginationEntity pageEntity = new StudentTransactionsPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Transaction> fromEntitiesToList(List<StudentTransactionsEntity> entities) {
        List<Transaction> newFormat = new ArrayList<>();
        if (entities != null) {
            for (StudentTransactionsEntity item : entities) {
                newFormat.add(new Transaction(
                        item.order_id,
                        TextUtil.formatRupiah(Integer.parseInt(item.amount)),
                        item.midtrans_data,
                        item.status,
                        item.created_at
                ));
            }
        }

        return newFormat;
    }
}