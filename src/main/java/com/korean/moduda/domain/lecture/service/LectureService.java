package com.korean.moduda.domain.lecture.service;

import com.korean.moduda.domain.lecture.Lecture;
import com.korean.moduda.domain.lecture.LectureType;
import com.korean.moduda.domain.lecture.MemberLecture;
import com.korean.moduda.domain.lecture.dto.LectureCompleted;
import com.korean.moduda.domain.lecture.dto.LectureCompletionSummaryResponse;
import com.korean.moduda.domain.lecture.dto.LectureLastResponse;
import com.korean.moduda.domain.lecture.dto.LectureProgress;
import com.korean.moduda.domain.lecture.dto.LectureRepeatResponse;
import com.korean.moduda.domain.lecture.dto.LectureSpecialResponse;
import com.korean.moduda.domain.lecture.dto.LectureWeekdayResponse;
import com.korean.moduda.domain.lecture.dto.LectureWeekendReponse;
import com.korean.moduda.domain.lecture.dto.MemberLectureProgressResponse;
import com.korean.moduda.domain.lecture.entity.LastLecture;
import com.korean.moduda.domain.lecture.entity.RepeatLecture;
import com.korean.moduda.domain.lecture.entity.SpecialLecture;
import com.korean.moduda.domain.lecture.entity.WeekDayLecture;
import com.korean.moduda.domain.lecture.entity.WeekendLecture;
import com.korean.moduda.domain.lecture.repository.LectureLastRepository;
import com.korean.moduda.domain.lecture.repository.LectureRepeatRepository;
import com.korean.moduda.domain.lecture.repository.LectureRepository;
import com.korean.moduda.domain.lecture.repository.LectureSpecialRepository;
import com.korean.moduda.domain.lecture.repository.LectureWeekdayRepository;
import com.korean.moduda.domain.lecture.repository.LectureWeekendRepository;
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
    private final LectureWeekdayRepository lectureWeekdayRepository;
    private final LectureWeekendRepository lectureWeekendRepository;
    private final LectureLastRepository lectureLastRepository;
    private final LectureRepeatRepository lectureRepeatRepository;
    private final LectureSpecialRepository lectureSpecialRepository;

    public MemberLectureProgressResponse getUserLectureProgress(Member member, int year,
        int month) {
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
            lectureProgressList.add(
                new LectureCompleted(date.toString(), progress.getOrDefault(date, false)));
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


    public Object getLectureDetail(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
            .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND));

        LectureType type = lecture.getLectureType();
        Object object;
        switch (type) {
            case WEEKDAY -> object = new LectureWeekdayResponse(
                lectureWeekdayRepository.findByLectureId(lectureId)
                    .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND)));
            case WEEKEND -> object = new LectureWeekendReponse(
                lectureWeekendRepository.findByLectureId(lectureId)
                    .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND)));
            case LAST_DAY ->
                object = new LectureLastResponse(lectureLastRepository.findByLectureId(lectureId)
                    .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND)));
            case REPEAT_DAY -> object = new LectureRepeatResponse(
                lectureRepeatRepository.findByLectureId(lectureId)
                    .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND)));
            case SPECIAL_DAY -> object = new LectureSpecialResponse(
                lectureSpecialRepository.findByLectureId(lectureId)
                    .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND)));
            default -> throw new BaseException(LectureErrorCode.INVALID_LECTURE_TYPE);
        }

        return object;
    }

    public void completeLecture(Member member, Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
            .orElseThrow(() -> new BaseException(LectureErrorCode.LECTURE_NOT_FOUND));

        MemberLecture memberLecture = memberLectureRepository.findByMemberAndLecture(member,
                lecture)
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

    public void createLectureByType(LectureType type, Map<String, Object> lectureData) {
        Lecture tmpLecture = lectureRepository.save(
            Lecture.builder()
                .lectureType(type)
                .lectureDate(LocalDate.parse((String) lectureData.get("lectureDate")))
                .build()
        );

        switch (type) {
            case WEEKDAY -> {
                WeekDayLecture lecture = mapToWeekDayLecture(lectureData, tmpLecture);
                lectureWeekdayRepository.save(lecture);
            }
            case WEEKEND -> {
                WeekendLecture lecture = mapToWeekendLecture(lectureData, tmpLecture);
                lectureWeekendRepository.save(lecture);
            }
            case SPECIAL_DAY -> {
                SpecialLecture lecture = mapToSpecialLecture(lectureData, tmpLecture);
                lectureSpecialRepository.save(lecture);
            }
            case REPEAT_DAY -> {
                RepeatLecture lecture = mapToRepeatLecture(lectureData, tmpLecture);
                lectureRepeatRepository.save(lecture);
            }
            case LAST_DAY -> {
                LastLecture lecture = mapToLastLecture(lectureData, tmpLecture);
                lectureLastRepository.save(lecture);
            }
            default -> throw new BaseException(LectureErrorCode.INVALID_LECTURE_TYPE);
        }
    }

    private WeekDayLecture mapToWeekDayLecture(Map<String, Object> data, Lecture tmpLecture) {
        WeekDayLecture lecture = WeekDayLecture.builder()
            .lecture(tmpLecture)
            .section1Title((String) data.get("section1Title"))
            .section1Content((String) data.get("section1Content"))
            .section1ImageUrl((String) data.get("section1ImageUrl"))
            .section2Title((String) data.get("section2Title"))
            .section2AudioUrl((String) data.get("section2AudioUrl"))
            .section3Title((String) data.get("section3Title"))
            .section3Content((String) data.get("section3Content"))
            .section4Title((String) data.get("section4Title"))
            .section4Content((String) data.get("section4Content"))
            .section5Title((String) data.get("section5Title"))
            .section6Title((String) data.get("section6Title"))
            .section6Content1((String) data.get("section6Content1"))
            .section6Content2((String) data.get("section6Content2"))
            .section7Title((String) data.get("section7Title"))
            .section7Content1((String) data.get("section7Content1"))
            .section7Content2((String) data.get("section7Content2"))
            .section7Content3((String) data.get("section7Content3"))
            .section8Title((String) data.get("section8Title"))
            .section8Subject((String) data.get("section8Subject"))
            .section8Content((String) data.get("section8Content"))
            .section8VideoUrl((String) data.get("section8VideoUrl"))
            .build();
        lecture.setLectureDate(LocalDate.parse((String) data.get("lectureDate")));
        lecture.setLastDescription1((String) data.get("lastDescription1"));
        lecture.setLastDescription2((String) data.get("lastDescription2"));
        return lecture;
    }

    private WeekendLecture mapToWeekendLecture(Map<String, Object> data, Lecture tmpLecture) {
        WeekendLecture lecture = WeekendLecture.builder()
            .lecture(tmpLecture)
            .subject1((String) data.get("subject1"))
            .content1((String) data.get("content1"))
            .subject2((String) data.get("subject2"))
            .content2((String) data.get("content2"))
            .subject3((String) data.get("subject3"))
            .content3((String) data.get("content3"))
            .subject4((String) data.get("subject4"))
            .content4((String) data.get("content4"))
            .subject5((String) data.get("subject5"))
            .content5((String) data.get("content5"))
            .date1(LocalDate.parse((String) data.get("date1")))
            .imageUrl1((String) data.get("imageUrl1"))
            .audioUrl1((String) data.get("audioUrl1"))
            .korean1((String) data.get("korean1"))
            .chinese1((String) data.get("chinese1"))
            .date2(LocalDate.parse((String) data.get("date2")))
            .imageUrl2((String) data.get("imageUrl2"))
            .audioUrl2((String) data.get("audioUrl2"))
            .korean2((String) data.get("korean2"))
            .chinese2((String) data.get("chinese2"))
            .date3(LocalDate.parse((String) data.get("date3")))
            .imageUrl3((String) data.get("imageUrl3"))
            .audioUrl3((String) data.get("audioUrl3"))
            .korean3((String) data.get("korean3"))
            .chinese3((String) data.get("chinese3"))
            .date4(LocalDate.parse((String) data.get("date4")))
            .imageUrl4((String) data.get("imageUrl4"))
            .audioUrl4((String) data.get("audioUrl4"))
            .korean4((String) data.get("korean4"))
            .chinese4((String) data.get("chinese4"))
            .date5(LocalDate.parse((String) data.get("date5")))
            .imageUrl5((String) data.get("imageUrl5"))
            .audioUrl5((String) data.get("audioUrl5"))
            .korean5((String) data.get("korean5"))
            .chinese5((String) data.get("chinese5"))
            .build();
        lecture.setLectureDate(LocalDate.parse((String) data.get("lectureDate")));
        lecture.setLastDescription1((String) data.get("lastDescription1"));
        lecture.setLastDescription2((String) data.get("lastDescription2"));
        return lecture;
    }

    private SpecialLecture mapToSpecialLecture(Map<String, Object> data, Lecture tmpLecture) {
        SpecialLecture lecture = SpecialLecture.builder()
            .lecture(tmpLecture)
            .title((String) data.get("title"))
            .content((String) data.get("content"))
            .build();
        lecture.setLectureDate(LocalDate.parse((String) data.get("lectureDate")));
        lecture.setLastDescription1((String) data.get("lastDescription1"));
        lecture.setLastDescription2((String) data.get("lastDescription2"));
        return lecture;
    }

    private RepeatLecture mapToRepeatLecture(Map<String, Object> data, Lecture tmpLecture) {
        RepeatLecture lecture = RepeatLecture.builder()
            .lecture(tmpLecture)
            .title1((String) data.get("title1"))
            .content1((String) data.get("content1"))
            .title2((String) data.get("title2"))
            .content2((String) data.get("content2"))
            .videoUrl((String) data.get("videoUrl"))
            .title3((String) data.get("title3"))
            .build();
        lecture.setLectureDate(LocalDate.parse((String) data.get("lectureDate")));
        lecture.setLastDescription1((String) data.get("lastDescription1"));
        lecture.setLastDescription2((String) data.get("lastDescription2"));
        return lecture;
    }

    private LastLecture mapToLastLecture(Map<String, Object> data, Lecture tmpLecture) {
        LastLecture lecture = LastLecture.builder()
            .lecture(tmpLecture)
            .title((String) data.get("title"))
            .content((String) data.get("content"))
            .build();
        lecture.setLectureDate(LocalDate.parse((String) data.get("lectureDate")));
        lecture.setLastDescription1((String) data.get("lastDescription1"));
        lecture.setLastDescription2((String) data.get("lastDescription2"));
        return lecture;
    }
}
