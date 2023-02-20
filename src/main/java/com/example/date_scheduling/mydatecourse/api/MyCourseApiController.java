package com.example.date_scheduling.mydatecourse.api;

import com.example.date_scheduling.mydatecourse.dto.FindAllCourseDto;
import com.example.date_scheduling.mydatecourse.dto.MyCourseDto;
import com.example.date_scheduling.mydatecourse.dto.RequestDeleteDto;
import com.example.date_scheduling.mydatecourse.dto.ResponseCourseDTO;
import com.example.date_scheduling.mydatecourse.entity.MyDateCourse;
import com.example.date_scheduling.mydatecourse.service.MyCourseService;
import com.example.date_scheduling.post.dto.FindAllPostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Slf4j  // 로깅을 위해
@RequestMapping("/api/mycourses")
@RequiredArgsConstructor
@CrossOrigin    // 다른 서버의 요청 허용
public class MyCourseApiController {

    private final MyCourseService service;

    // 데이트 코스 목록 전체 조회 요청
    // 캘린더에서 날짜를 클릭하면 해당 날짜의 데이트 목록만 조회됨
//    @GetMapping("/mycourse/{meetingDate}")
//    // 토큰 인증 필요
//    public ResponseEntity<?> myCourseList(@AuthenticationPrincipal String username,@PathVariable String meetingDate) {
//        log.info("/api/mycourses GET request!");
//        return ResponseEntity.ok().body(service.findAllServ(username, meetingDate));
//    }

//    @GetMapping("/mycourse/{meetingDate}")
//    // 토큰 인증 필요
//    public ResponseEntity<?> myCourseList(@AuthenticationPrincipal String username,@PathVariable String meetingDate) {
//        log.info("/api/mycourses GET request!");
//        return ResponseEntity.ok().body(service.findAllPostIdServ   (username, meetingDate));
//    }


    // 데이트 코스 개별 조회 요청
//    @GetMapping("/{courseId}")
//    public ResponseEntity<?> findOneCourse(@PathVariable String courseId) {
//        log.info("/api/mycourses GET request!", courseId);
//
//        // 해당 날짜에 데이트 코스가 없을 경우
//        if (courseId == null) return ResponseEntity.badRequest().build();
//
//        MyCourseDto dto = new MyCourseDto(service.findOneServ(courseId));
//
//        if (dto == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok().body(dto);
//    }

    // 데이트 코스 등록 요청
    @PostMapping
    public ResponseEntity<?> registerCourse(@RequestBody MyDateCourse newCourse, @AuthenticationPrincipal String username) {

        newCourse.setUsername(username);
        log.info("/api/mycourses POST request! - {}", newCourse);

        try {
            ResponseCourseDTO dto = service.createServ(newCourse);

            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 데이트 코스 수정 요청
//    @PutMapping
//    public ResponseEntity<?> updateCourse(@RequestBody MyDateCourse myDateCourse) {
//
//        log.info("/api/mycourses PUT request!", myDateCourse);
//
//        try {
//            FindAllCourseDto dtos = service.update(myDateCourse);
//            return ResponseEntity.ok().body(dtos);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    // 데이트 코스 삭제 요청
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@RequestBody MyDateCourse deleteCourse, @PathVariable String courseId, @AuthenticationPrincipal String username) {

        deleteCourse.setCourseId(courseId);
        log. info("/api/mycourses DELETE - {}", deleteCourse);

        try {
            ResponseCourseDTO dtos = service.deleteServ(deleteCourse, username);
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    ///////////////////////////////////////////////////////////////
    //날짜에 해당하는 저장된 일정들 보여주기
    @GetMapping("/mycourse/{meetingDate}")
    public ResponseCourseDTO myCourseList(@AuthenticationPrincipal String username, @PathVariable String meetingDate) {
        log.info("/api/mycourses/{} GET request!", meetingDate);
        return service.findAllMyCourseServ(username, meetingDate);
    }

    //이미 등록된 일정인지 확인
    @PostMapping("/check")
    public ResponseEntity<?> checkPostId(@RequestBody MyDateCourse myDateCourse, @AuthenticationPrincipal String username){
        boolean flag = service.checkDuplicateServ(myDateCourse, username);
        log.info("{} 중복여부?? - {}", myDateCourse.getPostId(), flag);
        return ResponseEntity.ok().body(flag);

    }

    // 데이트 코스 삭제 요청
//    @DeleteMapping("/{courseId}")
//    public ResponseEntity<?> deleteCourse(@PathVariable String courseId, @AuthenticationPrincipal String username) {
//
//        log. info("/api/mycourses DELETE - {}", courseId);
//
//        try {
//            boolean flag = service.deleteServ(courseId, username);
//            return ResponseEntity.ok().body(flag);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
