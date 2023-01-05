package com.example.date_scheduling.mydatecourse.dto;

import com.example.date_scheduling.mydatecourse.entity.MyDateCourse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
// 전체 데이트 코스 목록 보여줄 때 필요한 객체 in 마이페이지
public class FindAllCourseDto {

    private int count;  // 전체 데이트 코스 목록 갯수
    private List<MyCourseDto> myCourseDtos;

    public FindAllCourseDto(List<MyDateCourse> courseList) {
        this.count = courseList.size();
        this.convertDtoList(courseList);
    }

    // List<MyDateCourse>를 받으면 List<MyCourseDto>로 변환하는 메서드
    public void convertDtoList(List<MyDateCourse> courseList) {
        List<MyCourseDto> dtos = new ArrayList<>();

        for(MyDateCourse myDateCourse : courseList) {
            dtos.add(new MyCourseDto(myDateCourse));
        }
        this.myCourseDtos = dtos;
    }
}
