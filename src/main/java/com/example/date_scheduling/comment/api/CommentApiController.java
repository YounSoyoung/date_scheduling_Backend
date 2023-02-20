package com.example.date_scheduling.comment.api;

import com.example.date_scheduling.comment.dto.FindAllCommentDTO;
import com.example.date_scheduling.comment.dto.JustContent;
import com.example.date_scheduling.comment.dto.PostandCommentid;
import com.example.date_scheduling.comment.entity.CommentEntity;
import com.example.date_scheduling.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentApiController {

    private final CommentService service;

    @GetMapping("/{postid}")
    public FindAllCommentDTO show(@PathVariable String postid){
        log.info("GET MAPPING - COMMENTENTITY {}",postid);
        return service.showServ(postid);
    }

    @GetMapping("/comment/{commentid}") //
    public ResponseEntity<?> showOne(@PathVariable String commentid){
        return ResponseEntity.ok().body(service.showOneServ(commentid));
    }

    @PostMapping("/{postId}") // /comment/{postId}
    public ResponseEntity<?> save(@AuthenticationPrincipal String username, @PathVariable String postId, @RequestBody CommentEntity commentEntity){
        try{
//            CommentEntity commentEntity = new CommentEntity();

        //지금은 임의로 설정한것임.
            commentEntity.setPostid(postId);
            commentEntity.setUserid(username);
            log.info("/api/comments/{} POST request - {}", postId, commentEntity);
            return ResponseEntity.ok().body(service.saveServ(commentEntity));
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{commentid}")
    public ResponseEntity<?> modify(@AuthenticationPrincipal String username, @RequestBody CommentEntity commentEntity, @PathVariable String commentid){
        commentEntity.setCommentid(commentid);
        commentEntity.setUserid(username);

        log.info("/api/comments PUT request - {}",commentEntity);

        try{
            
            return ResponseEntity.ok().body(service.modifyServ(commentEntity));
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal String username,@RequestBody CommentEntity commentEntity){
        String commentUser = commentEntity.getUserid();
        if(commentUser.equals(username)){
            log.info("로그인한 계정이름과 삭제 요청한 계정이름이 동일하다 - 삭제 가능");
            commentEntity.setUserid(username);
        }else log.info("삭제 권한이 없습니다");

        try{
            FindAllCommentDTO commentEntities = service.deleteServ(commentEntity.getPostid(), commentEntity.getCommentid());
            log.info("/api/comments DELETE request -{}", commentEntity.getCommentid());
            return ResponseEntity.ok().body(commentEntities);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
