package com.korean.moduda.domain.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureCompleted {
    private String date;
    private boolean completed;
}
