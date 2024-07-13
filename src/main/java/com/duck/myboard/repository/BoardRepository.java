package com.duck.myboard.repository;


import com.duck.myboard.domain.Board;
import com.duck.myboard.mapper.BoardMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BoardRepository {

    private final BoardMapper boardMapper;

    public List<Board> findAll() {
        return boardMapper.findAll();
    }

}
