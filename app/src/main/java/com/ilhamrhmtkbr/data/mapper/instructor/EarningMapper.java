package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorEarningsResponse;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Earning;

import java.util.ArrayList;
import java.util.List;

public class EarningMapper {
    public static List<InstructorEarningsEntity> fromResponseToEarningListEntities(InstructorEarningsResponse response) {
        List<InstructorEarningsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorEarningsResponse.EarningItem item : response.data) {
                InstructorEarningsEntity earningEntity = new InstructorEarningsEntity();
                earningEntity.order_id = item.order_id;
                if (item.instructor_course != null) {
                    earningEntity.instructor_course = item.instructor_course.title;
                }
                earningEntity.amount = item.amount;
                earningEntity.status = item.status;
                earningEntity.created_at = item.created_at;
                earningEntity.student_full_name = item.student_full_name;
                newFormat.add(earningEntity);
            }
        }

        return newFormat;
    }

    public static List<InstructorEarningsPaginationEntity> fromResponseToEarningListPaginationEntities(InstructorEarningsResponse response) {
        List<InstructorEarningsPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.meta != null && response.meta.links != null) {
            for (InstructorEarningsResponse.Page item : response.meta.links) {
                InstructorEarningsPaginationEntity pageEntity = new InstructorEarningsPaginationEntity();
                pageEntity.url = item.url;
                pageEntity.label = item.label;
                pageEntity.isActive = item.active != null ? item.active : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Earning> fromEntitiesToList(List<InstructorEarningsEntity> instructorEarningsEntities) {
        List<Earning> newFormat = new ArrayList<>();
        if (instructorEarningsEntities != null) {
            for (InstructorEarningsEntity item : instructorEarningsEntities) {
                newFormat.add(new Earning(
                        item.order_id,
                        item.instructor_course,
                        item.student_full_name,
                        TextUtil.formatRupiah(Integer.parseInt(item.amount)),
                        TextUtil.capitalize(item.status),
                        item.created_at));
            }
        }

        return newFormat;
    }
}