package com.ilhamrhmtkbr.data.mapper.guest;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCourseDetailResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCoursesResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseMapper {
    public static List<Course> fromResponseToCourseList(PublicCoursesResponse response) {
        List<Course> newFormat = new ArrayList<>();
        for (PublicCoursesResponse.Course responseCourse : response.data) {
            Course course = new Course();
            course.setId(responseCourse.id);
            course.setTitle(TextUtil.capitalize(responseCourse.title));
            course.setImage(responseCourse.image);
            course.setInstructorId(responseCourse.instructor);
            course.setPrice(TextUtil.formatRupiah(responseCourse.price));
            course.setEditor(TextUtil.capitalize(responseCourse.editor));

            newFormat.add(course);
        }

        return newFormat;
    }

    public static Course fromResponseToCourseDetail(PublicCourseDetailResponse response) {
        Course newFormat = new Course();
        if (response != null && response.course != null) {
            newFormat.setId(response.course.id);
            newFormat.setTitle(TextUtil.capitalize(response.course.title));
            newFormat.setDescription(TextUtil.capitalize(response.course.description));
            newFormat.setImage(response.course.image);

            String instructorName = (response.course.instructor != null &&
                    response.course.instructor.user != null &&
                    response.course.instructor.user.full_name != null)
                    ? response.course.instructor.user.full_name
                    : "Unknown Instructor";
            newFormat.setInstructorId(TextUtil.capitalize(instructorName));

            newFormat.setLevel(TextUtil.capitalize(response.course.level));
            newFormat.setStatus(TextUtil.capitalize(response.course.status));

            try {
                if (response.course.price != null && !response.course.price.isEmpty()) {
                    newFormat.setPrice(TextUtil.formatRupiah(Integer.parseInt(response.course.price)));
                } else {
                    newFormat.setPrice("Rp 0");
                }
            } catch (NumberFormatException e) {
                newFormat.setPrice("Rp 0");
            }

            newFormat.setNotes(TextUtil.capitalize(response.course.notes));
            newFormat.setRequirements(TextUtil.capitalize(response.course.requirements));
            newFormat.setLikes(String.valueOf(response.likes));
            newFormat.setIsLikes(response.isLikes != null && response.isLikes);
        }

        return newFormat;
    }
}
