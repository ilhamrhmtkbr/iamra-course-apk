package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.data.local.entity.InstructorSectionsEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorSectionsResponse;
import com.ilhamrhmtkbr.domain.model.common.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionMapper {
    public static List<InstructorSectionsEntity> fromResponseToEntities(InstructorSectionsResponse response) {
        List<InstructorSectionsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorSectionsResponse.SectionItem item : response.data) {
                InstructorSectionsEntity section = new InstructorSectionsEntity();
                section.id = item.id;
                section.title = item.title;
                section.order_in_course = item.order_in_course;
                newFormat.add(section);
            }
        }
        return newFormat;
    }

    public static List<Section> fromEntitiesToList(List<InstructorSectionsEntity> entities){
        List<Section> newFormat = new ArrayList<>();
        if (entities != null) {
            for (InstructorSectionsEntity item: entities) {
                Section section = new Section();
                section.setId(item.id);
                section.setTitle(item.title);
                section.setOrderInCourse(item.order_in_course);
                newFormat.add(section);
            }
        }

        return newFormat;
    }
}
