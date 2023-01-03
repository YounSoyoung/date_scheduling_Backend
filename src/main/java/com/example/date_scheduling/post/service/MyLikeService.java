package com.example.date_scheduling.post.service;

import com.example.date_scheduling.post.dto.FindAllPostDto;
import com.example.date_scheduling.post.entity.MyLike;
import com.example.date_scheduling.post.entity.Post;
import com.example.date_scheduling.post.repository.MyLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyLikeService {
    private final MyLikeRepository myLikeRepository;

    //좋아요 기능 (추가)
    public boolean addLikeServ(String postId, String username){
        return myLikeRepository.addLike(postId, username);
    }

    //좋아요한 게시글 아이디 조회 처리
    public List<String> findAllPostIdServ(String username){
        return myLikeRepository.findAllPostId(username);
    }

    //좋아요 여부 확인
    public boolean checkLikeServ(String postId, String username) {return  myLikeRepository.checkMyLike(postId, username);}
}
