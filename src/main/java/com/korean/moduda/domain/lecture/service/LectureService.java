package com.korean.moduda.domain.lecture.service;

import com.korean.moduda.domain.lecture.Lecture;
import com.korean.moduda.domain.lecture.MemberLecture;
import com.korean.moduda.domain.lecture.dto.LectureCompletionSummaryResponse;
import com.korean.moduda.domain.lecture.dto.LectureDetailResponse;
import com.korean.moduda.domain.lecture.dto.LectureProgress;
import com.korean.moduda.domain.lecture.dto.MemberLectureProgressResponse;
import com.korean.moduda.domain.lecture.repository.LectureRepository;
import com.korean.moduda.domain.lecture.repository.MemberLectureRepository;
import com.korean.moduda.domain.member.Member;
import com.korean.moduda.domain.member.repository.MemberRepository;
import com.korean.moduda.global.exception.BaseException;
import com.korean.moduda.global.exception.errorCode.LectureErrorCode;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureService {

    private final MemberRepository memberRepository;
    private final MemberLectureRepository memberLectureRepository;
    private final LectureRepository lectureRepository;

    public MemberLectureProgressResponse getUserLectureProgress(Member member, int year,
        int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        // 해당 월의 모든 날짜를 저장할 리스트를 초기화합니다.
        List<LocalDate> allDates = new ArrayList<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            allDates.add(yearMonth.atDay(day)); // 1일부터 마지막 날까지 날짜를 추가합니다.
        }

        // 회원의 강의 정보를 가져옵니다.
        List<MemberLecture> lectures = memberLectureRepository.findByMemberAndLecture_LectureDateBetween(
            member,
            yearMonth.atDay(1),
            yearMonth.atEndOfMonth()
        );

        // 수강 여부를 저장할 맵을 초기화합니다.
        Map<LocalDate, Boolean> progress = new HashMap<>();

        // lectures에서 수강한 날짜와 완료 여부를 맵으로 저장합니다.
        for (MemberLecture lecture : lectures) {
            LocalDate lectureDate = lecture.getLecture().getLectureDate();
            Boolean isCompleted = lecture.isCompleted();
            progress.put(lectureDate, isCompleted);
        }

        // 모든 날짜에 대해 미수강으로 초기화하고, 수강한 경우에는 완료 여부를 반영합니다.
        Map<LocalDate, Boolean> resultProgress = new LinkedHashMap<>();
        for (LocalDate date : allDates) {
            // 수강 여부를 확인하여 결과 맵에 추가합니다.
            if (progress.containsKey(date)) {
                resultProgress.put(date, progress.get(date)); // 수강한 경우
            } else {
                resultProgress.put(date, false); // 미수강인 경우
            }
        }

        return new MemberLectureProgressResponse(resultProgress);
    }

    public LectureCompletionSummaryResponse getLectureCompletionSummary(LocalDate date) {
        YearMonth yearMonth = YearMonth.from(date);
        LocalDate weekStart = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<Member> members = memberRepository.findAllByDeleted();

        List<LectureProgress> weeklyProgressList = new ArrayList<>();
        List<LectureProgress> monthlyProgressList = new ArrayList<>();

        for (Member member : members) {
            // 주간 진행률 계산
            long totalLecturesInWeek = lectureRepository.countByLectureDateBetween(weekStart,
                weekEnd);
            long completedLecturesInWeek = memberLectureRepository.countCompletedByMemberAndLectureDateBetween(
                member, weekStart, weekEnd);
            double weeklyProgress = totalLecturesInWeek == 0 ? 0.0
                : (completedLecturesInWeek * 100.0 / totalLecturesInWeek);

            // 월간 진행률 계산
            long totalLecturesInMonth = lectureRepository.countByLectureDateBetween(
                yearMonth.atDay(1), yearMonth.atEndOfMonth());
            long completedLecturesInMonth = memberLectureRepository.countCompletedByMemberAndLectureDateBetween(
                member, yearMonth.atDay(1), yearMonth.atEndOfMonth());
            double monthlyProgress = totalLecturesInMonth == 0 ? 0.0
                : (completedLecturesInMonth * 100.0 / totalLecturesInMonth);

            weeklyProgressList.add(
                new LectureProgress(member.getId(), member.getName(), weeklyProgress));
            monthlyProgressList.add(
                new LectureProgress(member.getId(), member.getName(), monthlyProgress));
        }

        return new LectureCompletionSummaryResponse(weeklyProgressList, monthlyProgressList);
    }


    public LectureDetailResponse getLectureDetail(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
            .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND));
        return new LectureDetailResponse(
            lecture.getId(),
            lecture.getLectureType(),
            lecture.getDescription1(),
            lecture.getDescription2(),
            lecture.getDescription3(),
            lecture.getDescription4(),
            lecture.getDescription5(),
            lecture.getDescription6(),
            lecture.getDescription7(),
            lecture.getDescription8(),
            lecture.getLectureDate()
        );
    }

    public void completeLecture(Member member, Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
            .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND));

        MemberLecture memberLecture = memberLectureRepository.findByMemberAndLecture(member, lecture)
            .orElseGet(() -> {
                MemberLecture newMemberLecture = MemberLecture.builder()
                    .member(member)
                    .lecture(lecture)
                    .completed(true)
                    .build();
                return memberLectureRepository.save(newMemberLecture);
            });

        if (!memberLecture.isCompleted()) {
            memberLecture.setCompleted();
            memberLectureRepository.save(memberLecture);
        }
    }
}
