package com.korean.moduda.domain.lecture.controller;

import com.korean.moduda.domain.lecture.dto.LectureCompletionSummaryResponse;
import com.korean.moduda.domain.lecture.dto.LectureDetailResponse;
import com.korean.moduda.domain.lecture.dto.MemberLectureProgressResponse;
import com.korean.moduda.domain.lecture.service.LectureService;
import com.korean.moduda.domain.member.Member;
import com.korean.moduda.global.security.CurrentMember;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
public class GetLectureController {

    private final LectureService lectureService;

    @GetMapping("/progress/{year}/{month}")
    public ResponseEntity<MemberLectureProgressResponse> getUserLectureProgress(
        @CurrentMember Member member, @PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(lectureService.getUserLectureProgress(member, year, month));
    }

    @GetMapping("/progress-summary/{date}")
    public ResponseEntity<LectureCompletionSummaryResponse> getLectureCompletionSummary(
        @PathVariable LocalDate date) {
        return ResponseEntity.ok(lectureService.getLectureCompletionSummary(date));
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailResponse> getLectureDetail(@PathVariable Long lectureId) {
        return ResponseEntity.ok(lectureService.getLectureDetail(lectureId));
    }

    @PostMapping("/{lectureId}/complete")
    public ResponseEntity<Void> completeLecture(@CurrentMember Member member, @PathVariable Long lectureId) {
        lectureService.completeLecture(member, lectureId);
        return ResponseEntity.ok().build();
    }
}
