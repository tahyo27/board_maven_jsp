package com.duck.myboard.service;

import com.duck.myboard.domain.Comments;
import com.duck.myboard.repository.CommentsRepository;
import com.duck.myboard.request.CommentsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;

    void write(CommentsRequest commentsRequest) {
        Comments comments = Comments.builder()
                .author("ㅁ")
                .boardId(1L)
                .content("df")
                .parentId(2L)
                .build();
        commentsRepository.save(comments);
    }

    void getList(Long boardId) {
        commentsRepository.findById(1L);
    }

}
