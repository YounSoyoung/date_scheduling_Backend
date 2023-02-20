package com.example.date_scheduling.comment.service;

import com.example.date_scheduling.comment.dto.FindAllCommentDTO;
import com.example.date_scheduling.comment.entity.CommentEntity;
import com.example.date_scheduling.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;

    //postid와 일치하는 모든 코멘트들을 보여주기
    public FindAllCommentDTO showServ(String postid){
        return new FindAllCommentDTO(repository.show(postid));
    }

    //commentid와 일치하는 하나의 코멘트를 보여주기
    public CommentEntity showOneServ(String commentid){
        return repository.showOne(commentid);
    }

    //해당하는 postid 안에서 댓글을 달기
    public FindAllCommentDTO saveServ(CommentEntity commentEntity){
        if (commentEntity == null || commentEntity.getCommentid() == null){
            throw new RuntimeException("commentEntity에 문제가 있습니다.");
        }
        boolean save = repository.save(commentEntity);
        return save ? showServ(commentEntity.getPostid()) : null;
    }

    //댓글을 수정하기
    public CommentEntity modifyServ(CommentEntity commentEntity){
        boolean modify = repository.modify(commentEntity);
        if (!modify){
            log.warn("modify fail! Commententity를 잘못 입력한 가능성이 큽니다.");
        }
        return modify ? showOneServ(commentEntity.getCommentid()) : null;
    }

    //댓글 삭제하기
    public FindAllCommentDTO deleteServ(String postid, String commentid){
        boolean flag = repository.delete(commentid);

        if (!flag){
            log.warn("delete fail! not found id [{}]", commentid);
            throw new RuntimeException("delete fail!");
        }

        return showServ(postid);
    }

}
