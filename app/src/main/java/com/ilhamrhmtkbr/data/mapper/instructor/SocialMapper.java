package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.data.local.entity.InstructorSocialsEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorSocialsResponse;
import com.ilhamrhmtkbr.domain.model.instructor.Social;

import java.util.ArrayList;
import java.util.List;

public class SocialMapper {
    public static List<InstructorSocialsEntity> fromResponseToEntities(InstructorSocialsResponse response) {
        List<InstructorSocialsEntity> socials = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorSocialsResponse.SocialItem item : response.data) {
                InstructorSocialsEntity social = new InstructorSocialsEntity();
                social.id = item.id;
                social.app_name = item.app_name;
                social.display_name = item.display_name;
                social.url_link = item.url_link;
                socials.add(social);
            }
        }

        return socials;
    }

    public static List<Social> fromEntitiesToList(List<InstructorSocialsEntity> entities) {
        List<Social> socials = new ArrayList<>();
        if (entities != null) {
            for (InstructorSocialsEntity item: entities) {
                socials.add(new Social(
                        item.id,
                        item.url_link,
                        item.app_name,
                        item.display_name
                ));
            }
        }

        return socials;
    }
}
