package com.ilhamrhmtkbr.data.mapper.forum;

import com.ilhamrhmtkbr.data.remote.dto.response.ForumGroupResponse;
import com.ilhamrhmtkbr.domain.model.forum.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupMapper {
    public static List<Group> fromResponseList(List<ForumGroupResponse> responses) {
        List<Group> newFormat = new ArrayList<>();
        if (responses != null) {
            for (ForumGroupResponse response : responses) {
                newFormat.add(new Group(
                        response.id,
                        response.title,
                        response.description,
                        response.image,
                        response.editor
                ));
            }
        }
        return newFormat;
    }
}
