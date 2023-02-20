package com.example.date_scheduling.comment.dto;

import com.example.date_scheduling.comment.entity.CommentEntity;
import lombok.*;

import java.util.List;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class FindAllCommentDTO {
    private int count;
    private List<CommentEntity> comments;

    public FindAllCommentDTO(List<CommentEntity> commentList){
        this.count = commentList.size();
        this.comments = commentList;
    }
}
