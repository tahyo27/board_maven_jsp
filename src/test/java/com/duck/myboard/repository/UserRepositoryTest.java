package com.duck.myboard.repository;

import com.duck.myboard.domain.User;
import com.duck.myboard.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserMapper userMapper;


    @Test
    @DisplayName("User save 테스트")
    void user_save_test() {
        //given
        User user = User.builder()
                .email("asdf@naver.com")
                .name("psyduck")
                .password("1234")
                .build();

        //when
        int reusult = userMapper.save(user);

        //then
        Assertions.assertEquals(reusult, 1);



    }
}