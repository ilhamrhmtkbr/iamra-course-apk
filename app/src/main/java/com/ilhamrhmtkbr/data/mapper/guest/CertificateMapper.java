package com.ilhamrhmtkbr.data.mapper.guest;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCertificateVerifyResponse;
import com.ilhamrhmtkbr.domain.model.common.Certificate;

public class CertificateMapper {
    public static Certificate fromResponse(PublicCertificateVerifyResponse response) {
        Certificate certificate = new Certificate();
        certificate.setInstructorCourseTitle(TextUtil.capitalize(response.instructor_course.title));
        certificate.setCreatedAt(response.certificate_at);
        certificate.setStudentName(TextUtil.capitalize(response.student.user.full_name));
        return certificate;
    }
}
