package com.duck.myboard.domain;

import lombok.ToString;

@ToString
public class Board {
    private Long id;
    private String title;
    private String content;
    private String author;

}
