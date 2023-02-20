package com.example.date_scheduling.comment.repository;

import com.example.date_scheduling.comment.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentRepository {

    //댓글 달기
    boolean save(CommentEntity commentEntity);

    //댓글 삭제하기
    boolean delete(String commentid);

    //댓글 수정하기
    boolean modify(CommentEntity commentEntity);

    //한 post 안에 해당하는 모든 댓글 보여주기
    List<CommentEntity> show(String postid);

    //유일한 commentid에 해당하는 코멘트를 하나 가져오기
    CommentEntity showOne(String commentid);
}