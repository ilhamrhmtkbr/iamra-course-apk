package com.ilhamrhmtkbr.data.mapper;

import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.model.User;

public class UserMapper {
    public static User toModel(UserResponse response) {
        User user = new User();
        user.setUsername(response.getUsername());
        user.setFullName(response.getFull_name());
        user.setImage(response.getImage());
        user.setEmail(response.getEmail());
        user.setEmailVerifiedAt(response.getEmailVerifiedAt());
        user.setRole(response.getRole());
        user.setDob(response.getDob());
        user.setAddress(response.getAddress());
        user.setCreatedAt(response.getCreatedAt());
        user.setSummary(response.getSummary());
        user.setCategory(response.getCategory());
        user.setResume(response.getResume());
        return user;
    }
}
