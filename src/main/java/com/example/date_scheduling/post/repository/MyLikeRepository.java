package com.example.date_scheduling.post.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyLikeRepository {

    //좋아요한 게시글의 아이디를 저장소에 저장
    boolean addLike(String postId, String username);

    //좋아요한 게시글의 아이디 조회
    List<String> findAllPostId(String username);

    //좋아요 여부 확인
    boolean checkMyLike(String postId, String username);

    //좋아요한 게시글 삭제
    boolean removeMyLike(String username, String postId);

}
