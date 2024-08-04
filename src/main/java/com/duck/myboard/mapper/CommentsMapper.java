package com.duck.myboard.mapper;

import com.duck.myboard.domain.Comments;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentsMapper {

    int save(Comments comments);

    List<Comments> findByBoardId(Long boardId);
}
