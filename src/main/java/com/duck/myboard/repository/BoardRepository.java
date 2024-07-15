package com.duck.myboard.repository;


import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final BoardMapper boardMapper;

    public List<Board> findAll() {
        return boardMapper.findAll();
    }

    public int save(Board board) {
        return boardMapper.save(board);
    }

}
