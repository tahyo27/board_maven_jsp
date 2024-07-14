package com.duck.myboard.repository;

import com.duck.myboard.domain.User;
import com.duck.myboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserMapper userMapper;

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public int save(User user) {
        return userMapper.save(user);
    }
}
