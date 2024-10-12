package com.duck.myboard.service;

import com.duck.myboard.domain.Comments;
import com.duck.myboard.repository.CommentsRepository;
import com.duck.myboard.request.CommentsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;

    void write(CommentsRequest commentsRequest) {

        Comments comments = CommentsRequest.createComments(commentsRequest);

        commentsRepository.save(comments);
    }

    List<Comments> getList(Long boardId) {

        return commentsRepository.findById(1L);
    }

}
