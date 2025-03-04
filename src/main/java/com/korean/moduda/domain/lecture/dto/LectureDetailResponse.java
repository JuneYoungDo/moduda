package com.korean.moduda.domain.lecture.dto;

import com.korean.moduda.domain.lecture.LectureType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureDetailResponse {
    private Long id;
    private LectureType lectureType;
    private String description1;
    private String description2;
    private String description3;
    private String description4;
    private String description5;
    private String description6;
    private String description7;
    private String description8;
    private LocalDate lectureDate;
}
