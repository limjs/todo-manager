package com.subject.user;

import com.subject.user.controller.UserResponse;
import org.springframework.stereotype.Component;
import com.subject.user.repository.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setId(user.getId());
        return userResponse;
    }
}
