package com.subject.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.subject.user.UserMapper;
import com.subject.user.repository.User;
import com.subject.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/users")
    public List<UserResponse> searchUsers(
            @RequestBody UserRequest userRequest
    ) {
        List<User> users = userService.searchUsers(userRequest.toDto());
        return userMapper.toUserResponseList(users);
    }
}
