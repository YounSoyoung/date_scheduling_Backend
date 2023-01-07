package com.example.date_scheduling.mydatecourse.service;

import com.example.date_scheduling.mydatecourse.dto.FindAllCourseDto;
import com.example.date_scheduling.mydatecourse.dto.MyCourseDto;
import com.example.date_scheduling.mydatecourse.dto.RequestDeleteDto;
import com.example.date_scheduling.mydatecourse.dto.ResponseCourseDTO;
import com.example.date_scheduling.mydatecourse.entity.MyDateCourse;
import com.example.date_scheduling.mydatecourse.entity.ResponseCourse;
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


    public List<MyDateCourse> findAllServ(String username, String meetingDate) {
        log.info("username: {} / meetingDate: {}", username, meetingDate);

        return repository.findAll(username, meetingDate);
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
    public ResponseCourseDTO createServ(MyDateCourse newCourse) {

        if (newCourse == null) {
            log.warn("newCourse cannot be null!");
            throw new RuntimeException("newCourse cannot be null!");
        }

        boolean flag = repository.register(newCourse);
        if (flag) log.info("새로운 데이트 코스 [courseId : {}]이 저장되었습니다.", newCourse.getCourseId());

//        return flag ? findAllServ(newCourse.getUsername(), newCourse.getMeetingDate()) : null;
        return findAllMyCourseServ(newCourse.getUsername(), newCourse.getMeetingDate());

    }

    public ResponseCourseDTO deleteServ(MyDateCourse deleteCourse, String username) {
        if (deleteCourse == null) {
            log.warn("deleteCourse cannot be null!");
            throw new RuntimeException("deleteCourse cannot be null!");
        }

        log.info("{} 작성된 일정 삭제 - username: {}", deleteCourse, username);

        boolean flag = repository.remove(deleteCourse.getCourseId());
        log.info("삭제 성공 여부: {}", flag);

        return findAllMyCourseServ(username, deleteCourse.getMeetingDate());
    }
    
    // 데이트 코스 개별 조회
    public MyDateCourse findOneServ(String courseId) {

        MyDateCourse myDateCourse = repository.findOne(courseId);
        log.info("findOneServ return data - {}", myDateCourse);

        return myDateCourse != null ? myDateCourse : null;
    }


    /////////////////////////////////////////////////////
    //날짜를 선택하면 그 날짜에 해당하는 코스들 보여주기
    public ResponseCourseDTO findAllMyCourseServ(String meetingDate, String username){
        log.info("{}의 일정 중 {}에 해당하는 코스 조회", username, meetingDate);

        return new ResponseCourseDTO(repository.findAllMyCourse(username, meetingDate));
    }

}
