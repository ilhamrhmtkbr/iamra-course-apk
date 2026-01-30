package com.ilhamrhmtkbr.data.mapper.forum;

import com.ilhamrhmtkbr.data.remote.dto.response.ForumChatResponse;
import com.ilhamrhmtkbr.domain.model.forum.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatMapper {
    public static List<Chat> fromResponse(List<ForumChatResponse> responses) {
        List<Chat> newFormat = new ArrayList<>();
        if(responses != null) {
            for (ForumChatResponse response : responses) {
                Chat chat = new Chat();
                chat.setId(response.id);
                chat.setMessage(response.message);
                chat.setCourseId(response.course_id);
                chat.setName(response.name);
                chat.setUsername(response.username);
                chat.setRole(response.role);
                chat.setCreatedAt(response.created_at);
                newFormat.add(chat);
            }
        }

        return newFormat;
    }
}
