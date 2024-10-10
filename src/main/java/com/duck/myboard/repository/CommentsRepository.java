package com.duck.myboard.repository;

import com.duck.myboard.domain.Comments;
import com.duck.myboard.mapper.CommentsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class CommentsRepository {

    private final CommentsMapper commentsMapper;

    public void save(Comments comments) {
        commentsMapper.save(comments);
    }

    public void findById(Long boardId) {
        commentsMapper.findByBoardId(boardId);
    }
}
