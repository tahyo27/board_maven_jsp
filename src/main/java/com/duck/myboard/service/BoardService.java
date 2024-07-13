package com.duck.myboard.service;

import com.duck.myboard.domain.Board;
import com.duck.myboard.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    public List<Board> getList() {
        return boardRepository.findAll();
    }
}
