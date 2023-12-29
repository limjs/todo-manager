package com.subject.user.service;

import com.subject.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.subject.user.repository.User;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> searchUsers(SearchUserDto searchUserDto) {
        return userRepository.findAll();
    }

    public User save(User userEntity) {
        return userRepository.save(userEntity);
    }

}
