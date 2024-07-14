package com.duck.myboard.domain;

import lombok.Builder;
import lombok.ToString;

@ToString
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;

    @Builder
    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
