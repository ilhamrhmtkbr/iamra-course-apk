package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.data.local.entity.InstructorAccountEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorAccountResponse;
import com.ilhamrhmtkbr.domain.model.instructor.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountMapper {
    public static List<InstructorAccountEntity> fromResponseToEntities(InstructorAccountResponse response) {
        List<InstructorAccountEntity> newFormat = new ArrayList<>();

        if (response != null) {
           InstructorAccountEntity accountEntity = new InstructorAccountEntity();
           accountEntity.account_id = response.account_id;
           accountEntity.alias_name = response.alias_name;
           accountEntity.bank_name = response.bank_name;
            newFormat.add(accountEntity);
       }

        return newFormat;
    }

    public static Account fromEntityListToModel(List<InstructorAccountEntity> entities) {
        InstructorAccountEntity accountEntity = entities.get(0);
        return new Account( accountEntity.account_id, accountEntity.bank_name, accountEntity.alias_name);
    }
}
