package com.duck.myboard.service;

import com.duck.myboard.domain.Board;
import com.duck.myboard.repository.BoardRepository;
import com.duck.myboard.request.BoardRequest;
import com.duck.myboard.response.BoardResponse;
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

    public int write(BoardRequest boardRequest) {
        
        Board board = BoardRequest.createConvert(boardRequest);

        return boardRepository.save(board);
    }

    public int edit(Long boardId, BoardRequest boardRequest) {

        Board board = BoardRequest.editConvert(boardId, boardRequest);

        return boardRepository.update(board);
    }

    public int delete(Long boardId) {
        return boardRepository.deleteById(boardId);
    }

    public BoardResponse get(Long boardId) {
        Board board = boardRepository.findById(boardId);
        return BoardResponse.convert(board);
    }
}
