package com.example.date_scheduling.mydatecourse.repository;

import com.example.date_scheduling.mydatecourse.entity.MyDateCourse;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
// 나만의 데이트 코스를 CRUD 한다.
public interface MyDateCourseRepository {

    // 데이트 코스로 등록
    // 상세 게시물 페이지에서 등록
    boolean register(MyDateCourse dateCourse);

    //해당 날짜에 저장되어있는 postId들 조회
    List<String> findAllPostId(String username, String meetingDate);


    ////// <마이 페이지> ///////
    // 전체 데이트 코스 목록 조회 기능
    List<MyDateCourse> findAll(String username, String meetingDate);

    // 데이트 코스 개별 조회 기능
    MyDateCourse findOne(String courseId);

    // 데이트 코스 수정 기능
    boolean modify(MyDateCourse dateCourse);

    // 데이트 코스 삭제 기능
    boolean remove(String postId, String username, String meetingDate);
}
