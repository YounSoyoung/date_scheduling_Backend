package com.example.date_scheduling.comment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;


@Getter @Setter @ToString

@AllArgsConstructor
public class CommentEntity {
    //이 댓글만의 고유한 id
    private String commentid;
    //어떤 게시글에 답글을 달았는지 알기위한 post의 id
    // db에서 테이블을 작성할때 null가능하게 해야지 오류 안 뜸
    private String postid;
    //어떤 유저가 답글을 달았는지 알기위한 유저의 id
    private String userid;
    //댓글을 작성한 시간
    @JsonFormat(pattern = "yyyy-MM-dd a HH:mm", timezone = "Asia/Seoul")
    private Date regdate;
    //댓글의 내용
    private String content;

    public CommentEntity(){
        this.commentid = UUID.randomUUID().toString();
    }

    public CommentEntity(String postid, String userid, String content){
        this();
        this.postid=postid;
        this.userid=userid;
        this.content=content;
    }

    public CommentEntity(String content){
        this();
        this.content = content;
    }
}
