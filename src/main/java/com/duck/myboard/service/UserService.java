package com.duck.myboard.service;

import com.duck.myboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.duck.myboard.domain.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public int sign(User user) {
        return userRepository.save(user);
    }
}
