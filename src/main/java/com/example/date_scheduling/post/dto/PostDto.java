package com.example.date_scheduling.post.dto;

import com.example.date_scheduling.post.entity.Post;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
// 민감한 정보 빼고 공개 가능한 데이터
public class PostDto {

    private String userId;
    private String postId;
    private String title;
    private String content;
    private String image;
    private Date regDate;


    // Post에서 PostDto가 필요한 필드를 빼오는 생성자
    public PostDto(Post post) {
        this.userId = post.getUserId();
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();
        this.regDate = post.getRegDate();

    }
}
