package com.ilhamrhmtkbr.data.mapper.student;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCertificateResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCertificatesResponse;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.domain.model.student.CertificateDetail;

import java.util.ArrayList;
import java.util.List;

public class CertificateMapper {
    public static List<StudentCertificatesEntity> fromResponseToEntities(StudentCertificatesResponse response) {
        List<StudentCertificatesEntity> newFormat = new ArrayList<>();
        if (response != null) {
            for (StudentCertificatesResponse.CertificateItem item : response.data) {
                StudentCertificatesEntity certificateEntity = new StudentCertificatesEntity();
                certificateEntity.id = item.id;
                certificateEntity.course_title = item.instructor_course.title;
                certificateEntity.created_at = item.created_at;
                certificateEntity.student_name = item.student.user.full_name;
                newFormat.add(certificateEntity);
            }
        }

        return newFormat;
    }

    public static List<StudentCertificatesPaginationEntity> fromResponseToPaginationEntities(StudentCertificatesResponse response) {
        List<StudentCertificatesPaginationEntity> newFormat = new ArrayList<>();
        if (response != null) {
            for (Page item : response.links) {
                StudentCertificatesPaginationEntity pageEntity = new StudentCertificatesPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Certificate> fromEntitiesToList(List<StudentCertificatesEntity> entities) {
        List<Certificate> newFormat = new ArrayList<>();
        if (entities != null) {
            for (StudentCertificatesEntity item : entities) {
                Certificate certificate = new Certificate();
                certificate.setId(item.id);
                certificate.setInstructorCourseTitle(item.course_title);
                certificate.setStudentName(item.student_name);
                certificate.setCreatedAt(item.created_at);
                newFormat.add(certificate);
            }
        }
        return newFormat;
    }

    public static CertificateDetail fromResponseToCertificateDetail(StudentCertificateResponse response) {
        if (response != null && response.data != null) {
            StudentCertificateResponse.CertificateItem data = response.data;
            Certificate certificate = new Certificate();
            certificate.setId(data.id);
            certificate.setInstructorName(data.instructor);
            certificate.setCreatedAt(data.created_at);

            Course course = new Course();
            if (data.course != null) {
                course.setId(data.course.id);
                course.setTitle(data.course.title);
                course.setDescription(data.course.description);
                course.setImage(data.course.image);
                course.setLevel(TextUtil.capitalize(data.course.level));
                course.setPrice(TextUtil.formatRupiah(data.course.price));
            }

            List<Section> sections = new ArrayList<>();
            if (data.course != null && data.course.sections != null) {
                for (StudentCertificateResponse.InstructorSections item : data.course.sections) {
                    Section section = new Section();
                    section.setId(item.id);
                    section.setTitle(item.title);
                    sections.add(section);
                }
            }

            return new CertificateDetail(certificate, course, sections);

        } else {
            return new CertificateDetail(new Certificate(), new Course(), new ArrayList<>());
        }
    }
}
