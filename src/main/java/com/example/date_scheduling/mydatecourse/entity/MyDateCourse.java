package com.example.date_scheduling.mydatecourse.entity;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MyDateCourse {

    private String courseId;    // 데이트 코스 식별자
    private String postId;      // 게시물 식별자 - Post의 정보가 연결되어 나오도록
                                // 일단 게시물 제목(title)만 보이게
    private String username;    // 등록한 사람 닉네임 (자동으로 들어가게)
    private String meetingDate;    // 데이트하기로 지정한 날짜
    private Date regDate;       // 최초 등록 날짜

    public MyDateCourse() {

        this.courseId = UUID.randomUUID().toString();
    }
}
