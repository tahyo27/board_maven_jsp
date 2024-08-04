package com.duck.myboard.service;

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

    public Long write(CommentsRequest commentsRequest) {

        return 1L;
    }
}
