package com.korean.moduda.domain.lecture.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberLectureProgressResponse {
    private List<LectureCompleted> lectureProgressList;
}

