package com.example.date_scheduling.mydatecourse.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ResponseCourse {
    private String courseId;
    private String postId;
    private String title;
}
