package com.ilhamrhmtkbr.data.mapper.guest;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCourseSectionResponse;
import com.ilhamrhmtkbr.domain.model.common.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionMapper {
    public static List<Section> fromResponseToList(List<PublicCourseSectionResponse> response) {
        List<Section> newFormat = new ArrayList<>();
        if (response != null) {
            for (PublicCourseSectionResponse item : response) {
                Section section = new Section();
                section.setId(item.id);
                section.setTitle(TextUtil.capitalize(item.title));
                section.setOrderInCourse(item.order_in_course);
                newFormat.add(section);
            }
        }

        return newFormat;
    }
}
