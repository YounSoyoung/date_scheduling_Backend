package com.example.date_scheduling.mydatecourse.service;

import com.example.date_scheduling.mydatecourse.dto.FindAllCourseDto;
import com.example.date_scheduling.mydatecourse.dto.MyCourseDto;
import com.example.date_scheduling.mydatecourse.entity.MyDateCourse;
import com.example.date_scheduling.mydatecourse.repository.MyDateCourseRepository;
import com.example.date_scheduling.post.dto.FindAllPostDto;
import com.example.date_scheduling.post.entity.Post;
import com.example.date_scheduling.post.repository.PostRepository;
import com.example.date_scheduling.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyCourseService {

    private final MyDateCourseRepository repository;
    private final PostRepository postRepository;


    public FindAllCourseDto findAllServ(String username, String meetingDate) {
        log.info("username: {} / meetingDate: {}", username, meetingDate);

        return new FindAllCourseDto(repository.findAll(username, meetingDate));
    }

    public FindAllPostDto findAllPostIdServ(String username, String meetingDate){
        log.info("username: {} / meetingDate: {}", username, meetingDate);

        List<String> savedPostIds = repository.findAllPostId(username, meetingDate);
        List<Post> mySavedPosts = new ArrayList<>();

        for(String postId: savedPostIds){
            Post post = postRepository.findOne(postId);
            mySavedPosts.add(post);
        }
        return new FindAllPostDto(mySavedPosts);
    };


    // 새로운 데이트 코스 등록
    public FindAllPostDto createServ(final MyDateCourse newCourse) {

        if (newCourse == null) {
            log.warn("newCourse cannot be null!");
            throw new RuntimeException("newCourse cannot be null!");
        }

        boolean flag = repository.register(newCourse);
        if (flag) log.info("새로운 데이트 코스 [courseId : {}]이 저장되었습니다.", newCourse.getCourseId());

//        return flag ? findAllServ(newCourse.getUsername(), newCourse.getMeetingDate()) : null;
        return findAllPostIdServ(newCourse.getUsername(), newCourse.getMeetingDate());

    }
    
    // 데이트 코스 개별 조회
    public MyDateCourse findOneServ(String courseId) {

        MyDateCourse myDateCourse = repository.findOne(courseId);
        log.info("findOneServ return data - {}", myDateCourse);

        return myDateCourse != null ? myDateCourse : null;
    }

    // 데이트 코스 삭제
    public FindAllCourseDto deleteServ(String courseId) {
        MyDateCourse myDateCourse = findOneServ(courseId);

        boolean flag = repository.remove(courseId);

        // 삭제 실패한 경우
        if(!flag) {
            log.warn("delete fail! not found courseId [{}]", courseId);
            throw new RuntimeException("delete fail!");
        }

        return findAllServ(myDateCourse.getUsername(), myDateCourse.getMeetingDate());
    }

    // 데이트 코스 수정
//    public FindAllCourseDto update(MyDateCourse dateCourse) {
//
//        boolean flag = repository.modify(dateCourse);
//        return flag ? findAllServ() : new FindAllCourseDto();
//    }

}
