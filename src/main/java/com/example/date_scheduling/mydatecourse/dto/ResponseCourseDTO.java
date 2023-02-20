package com.example.date_scheduling.mydatecourse.dto;

import com.example.date_scheduling.mydatecourse.entity.MyDateCourse;
import com.example.date_scheduling.mydatecourse.entity.ResponseCourse;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCourseDTO {

    private int count;  // 전체 데이트 코스 목록 갯수
    private List<ResponseCourse> responseCourses;

    public ResponseCourseDTO(List<ResponseCourse> courseList) {
        this.count = courseList.size();
        this.responseCourses = courseList;
    }


}
