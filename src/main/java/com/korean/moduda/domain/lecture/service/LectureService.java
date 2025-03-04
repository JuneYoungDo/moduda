package com.korean.moduda.domain.lecture.service;

import com.korean.moduda.domain.lecture.Lecture;
import com.korean.moduda.domain.lecture.MemberLecture;
import com.korean.moduda.domain.lecture.dto.LectureCompleted;
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

    public MemberLectureProgressResponse getUserLectureProgress(Member member, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        List<LocalDate> allDates = new ArrayList<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            allDates.add(yearMonth.atDay(day));
        }

        List<MemberLecture> lectures = memberLectureRepository.findByMemberAndLecture_LectureDateBetween(
            member,
            yearMonth.atDay(1),
            yearMonth.atEndOfMonth()
        );

        Map<LocalDate, Boolean> progress = new HashMap<>();
        for (MemberLecture lecture : lectures) {
            progress.put(lecture.getLecture().getLectureDate(), lecture.isCompleted());
        }

        List<LectureCompleted> lectureProgressList = new ArrayList<>();
        for (LocalDate date : allDates) {
            lectureProgressList.add(new LectureCompleted(date.toString(), progress.getOrDefault(date, false)));
        }

        return new MemberLectureProgressResponse(lectureProgressList);
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
