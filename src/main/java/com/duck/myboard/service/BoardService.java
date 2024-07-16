package com.duck.myboard.service;

import com.duck.myboard.domain.Board;
import com.duck.myboard.repository.BoardRepository;
import com.duck.myboard.request.BoardCreate;
import com.duck.myboard.request.BoardEdit;
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

    public List<Board> getPagingList() {
        return boardRepository.getOffsetPaging();
    }

    public int write(BoardCreate boardCreate) {
        
        Board board = boardCreate.convert(boardCreate);

        return boardRepository.save(board);
    }

    public int edit(BoardEdit boardEdit) {

        Board board = boardEdit.convert(boardEdit);

        return boardRepository.update(board);
    }

    public int delete(Long boardId) {
        return boardRepository.deleteById(boardId);
    }

}
