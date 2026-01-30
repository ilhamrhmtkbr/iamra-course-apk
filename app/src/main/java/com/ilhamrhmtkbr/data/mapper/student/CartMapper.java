package com.ilhamrhmtkbr.data.mapper.student;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.local.entity.StudentCartsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCartsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCartResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartMapper {
    public static List<StudentCartsEntity> fromResponseToEntities(StudentCartResponse response) {
        List<StudentCartsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (StudentCartResponse.CartItem item : response.data) {
                StudentCartsEntity cartEntity = new StudentCartsEntity();
                cartEntity.id = item.id;
                cartEntity.instructorCourseId = item.instructor_course_id;
                cartEntity.instructorCourse = new StudentCartsEntity.InstructorCourse();
                if (item.instructor_course != null) {
                    cartEntity.instructorCourse.title = item.instructor_course.title;
                    cartEntity.instructorCourse.description = item.instructor_course.description;
                    cartEntity.instructorCourse.image = item.instructor_course.image;
                    cartEntity.instructorCourse.price = item.instructor_course.price;
                    cartEntity.instructorCourse.level = item.instructor_course.level;
                    cartEntity.instructorCourse.status = item.instructor_course.status;
                }
                newFormat.add(cartEntity);
            }
        }

        return newFormat;
    }

    public static List<StudentCartsPaginationEntity> fromResponseToPaginationEntities(StudentCartResponse response) {
        List<StudentCartsPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.links != null) {
            for (Page item : response.links) {
                StudentCartsPaginationEntity pageEntity = new StudentCartsPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Cart> fromEntitiesToList(List<StudentCartsEntity> entities) {
        List<Cart> newFormat = new ArrayList<>();
        if (entities != null) {
            for (StudentCartsEntity item : entities) {
                Course course = new Course();
                if (item.instructorCourse != null) {
                    course.setId(item.instructorCourse.id);
                    course.setTitle(item.instructorCourse.title);
                    course.setDescription(item.instructorCourse.description);
                    course.setImage(item.instructorCourse.image);
                    course.setStatus(TextUtil.capitalize(item.instructorCourse.status));
                    course.setLevel(TextUtil.capitalize(item.instructorCourse.level));
                    course.setPrice(TextUtil.formatRupiah(Integer.parseInt(item.instructorCourse.price)));
                }

                newFormat.add(new Cart(
                        item.id,
                        item.instructorCourseId,
                        course
                ));
            }
        }

        return newFormat;
    }
}