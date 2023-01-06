package com.example.date_scheduling.mydatecourse.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestDeleteDto {
    private String postId;
    private String meetingDate;
}
