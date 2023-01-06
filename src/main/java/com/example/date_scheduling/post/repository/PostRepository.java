package com.example.date_scheduling.post.repository;

import org.apache.ibatis.annotations.Mapper;
import com.example.date_scheduling.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
// 역할 : 게시물 데이터를 CRUD 한다(생성, 조회, 수정, 삭제).
public interface PostRepository {

    // 게시물 생성 기능

    /**
     * 게시물 데이터를 저장소에 저장하는 기능
     * @param post - 게시판 데이터의 집합
     * @return - 저장 성공 시 true, 실패 시 false
     */

    boolean save(Post post);

    // 전체 게시물 조회 기능
    List<Post> findAll();

    //마이페이지에서 게시글 리스트 조회 기능
    List<Post> findAllMyReviews(String userId);

    //카테고리별 게시글 조회 기능
    List<Post> findReviews(String cID);

    // 게시물 개별 조회 기능
    Post findOne(String postId);

    // 내가 작성한 게시물 개별 조회
    Post findOneMyPost(String postId, String userId);

    // 같은 카테고리를 가지고 있는 게시글 리스트(이미 검색한 게시글은 빼고 반환)
    List<Post> findOtherReviews(String cID, String postId);

    // 게시물 삭제 기능
    boolean remove(String postId);

    // 게시물 수정 기능
    boolean modify(Post post);
}
