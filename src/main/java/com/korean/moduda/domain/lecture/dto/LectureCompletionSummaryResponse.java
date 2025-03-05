package com.korean.moduda.domain.lecture.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureCompletionSummaryResponse {
    private List<LectureProgress> weeklyProgress; // 주간 진행률 리스트
    private List<LectureProgress> monthlyProgress; // 월간 진행률 리스트
}
