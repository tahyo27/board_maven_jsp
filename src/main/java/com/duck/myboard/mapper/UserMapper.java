package com.duck.myboard.mapper;

import com.duck.myboard.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int save(User user);
    List<User> findAll();
}
