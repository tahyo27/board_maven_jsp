package com.duck.myboard.mapper;

import com.duck.myboard.domain.Board;
import com.duck.myboard.request.BoardCreate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    int save(Board board);
    List<Board> findAll();
    int update(Board board);

    int deleteById(Long id);

    List<Board> getOffsetPaging();
}
