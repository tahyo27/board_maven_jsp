package com.duck.myboard.service;

import com.duck.myboard.domain.Board;
import com.duck.myboard.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    public List<Board> getList() {
        return boardRepository.findAll();
    }

    public int write(Board board) {
        return boardRepository.save(board);
    }
}
