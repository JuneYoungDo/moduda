package com.korean.moduda.domain.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureProgress {
    private Long memberId;
    private String memberName;
    private double progress;
}
