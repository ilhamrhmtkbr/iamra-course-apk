package com.ilhamrhmtkbr.domain.model.student;

import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Section;

import java.util.List;

public class CertificateDetail {
    private final Certificate certificate;
    private final Course course;
    private final List<Section> sections;

    public CertificateDetail(Certificate certificate, Course course, List<Section> sections) {
        this.certificate = certificate;
        this.course = course;
        this.sections = sections;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public Course getCourse() {
        return course;
    }

    public List<Section> getSections() {
        return sections;
    }
}
