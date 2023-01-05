package com.example.date_scheduling.mydatecourse.dto;

import com.example.date_scheduling.mydatecourse.entity.MyDateCourse;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
// 민감한 정보 빼고 공개 가능한 데이터
// postId 로 게시물 데이터 접근해서 (일단) title만 보여지게끔
public class MyCourseDto {

    private String username;    // 등록한 사람 닉네임
    private String postId;
    private String meetingDate;   // 데이트하기로 한 날짜
    private Date regDate;       // 코스로 최초 등록한 날짜

    // MyDateCourse에서 MyCourseDto가 필요한 필드를 빼오는 생성자
    public MyCourseDto(MyDateCourse myDateCourse) {
        this.username = myDateCourse.getUsername();
        this.postId = myDateCourse.getPostId();
        this.meetingDate = myDateCourse.getMeetingDate();
        this.regDate = myDateCourse.getRegDate();
    }
}
