package com.korean.moduda.domain.lecture.dto;

import java.time.LocalDate;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberLectureProgressResponse {
    private Map<LocalDate, Boolean> lectureProgress;
}
