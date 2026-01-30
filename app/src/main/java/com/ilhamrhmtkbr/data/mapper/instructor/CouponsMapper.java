package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCouponResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCouponsResponse;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;

import java.util.ArrayList;
import java.util.List;

public class CouponsMapper {
    public static List<InstructorCouponsEntity> fromResponseToEntities(InstructorCouponsResponse response) {
        List<InstructorCouponsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorCouponsResponse.CouponItem item : response.data) {
                InstructorCouponsEntity couponEntity = new InstructorCouponsEntity();
                couponEntity.id = item.id;
                couponEntity.discount = item.discount;
                couponEntity.expiry_date = item.expiry_date;
                couponEntity.max_redemptions = item.max_redemptions;
                newFormat.add(couponEntity);
            }
        }

        return newFormat;
    }

    public static List<InstructorCouponsPaginationEntity> fromResponseToPaginationEntities(InstructorCouponsResponse response) {
        List<InstructorCouponsPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.meta != null && response.meta.links != null) {
            for (InstructorCouponsResponse.Page item : response.meta.links) {
                InstructorCouponsPaginationEntity pageEntity = new InstructorCouponsPaginationEntity();
                pageEntity.url = item.url;
                pageEntity.label = item.label;
                pageEntity.isActive = item.active != null ? item.active : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Coupon> fromEntitiesToList(List<InstructorCouponsEntity> entities) {
        List<Coupon> newFormat = new ArrayList<>();
        if (entities != null) {
            for (InstructorCouponsEntity item : entities) {
                Coupon coupon = new Coupon();
                coupon.setId(item.id);
                coupon.setDiscount(item.discount);
                coupon.setExpiryDate(item.expiry_date);
                coupon.setMaxRedemptions(item.max_redemptions);
                newFormat.add(coupon);
            }
        }

        return newFormat;
    }

    public static Coupon fromResponseToModel(InstructorCouponResponse response) {
        Coupon coupon = new Coupon();
        if (response != null && response.data != null && response.data.instructor_course != null) {
            coupon.setId(response.data.id);
            coupon.setDiscount(response.data.discount);
            coupon.setExpiryDate(response.data.expiry_date);
            coupon.setMaxRedemptions(response.data.max_redemptions);
            coupon.setInstructorCourseTitle(response.data.instructor_course.title);
            coupon.setInstructorCourseStatus(TextUtil.capitalize(response.data.instructor_course.status));
        }

        return coupon;
    }
}
