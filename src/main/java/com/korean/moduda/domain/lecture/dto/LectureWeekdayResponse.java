package com.korean.moduda.domain.lecture.dto;

import com.korean.moduda.domain.lecture.entity.WeekDayLecture;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureWeekdayResponse {

    private LocalDate lectureDate;

    private String section1Title;
    private String section1Content;
    private String section1ImageUrl;

    private String section2Title;
    private String section2AudioUrl;

    private String section3Title;
    private String section3Content;

    private String section4Title;
    private String section4Content;

    private String section5Title;

    private String section6Title;
    private String section6Content1;
    private String section6Content2;

    private String section7Title;
    private String section7Content1;
    private String section7Content2;
    private String section7Content3;

    private String section8Title;
    private String section8Subject;
    private String section8Content;
    private String section8VideoUrl;

    private String lastDescription1;
    private String lastDescription2;

    public LectureWeekdayResponse(WeekDayLecture lecture) {
        this.lectureDate = lecture.getLectureDate();
        this.section1Title = lecture.getSection1Title();
        this.section1Content = lecture.getSection1Content();
        this.section1ImageUrl = lecture.getSection1ImageUrl();
        this.section2Title = lecture.getSection2Title();
        this.section2AudioUrl = lecture.getSection2AudioUrl();
        this.section3Title = lecture.getSection3Title();
        this.section3Content = lecture.getSection3Content();
        this.section4Title = lecture.getSection4Title();
        this.section4Content = lecture.getSection4Content();
        this.section5Title = lecture.getSection5Title();
        this.section6Title = lecture.getSection6Title();
        this.section6Content1 = lecture.getSection6Content1();
        this.section6Content2 = lecture.getSection6Content2();
        this.section7Title = lecture.getSection7Title();
        this.section7Content1 = lecture.getSection7Content1();
        this.section7Content2 = lecture.getSection7Content2();
        this.section7Content3 = lecture.getSection7Content3();
        this.section8Title = lecture.getSection8Title();
        this.section8Content = lecture.getSection8Content();
        this.section8VideoUrl = lecture.getSection8VideoUrl();
        this.lastDescription1 = lecture.getLastDescription1();
        this.lastDescription2 = lecture.getLastDescription2();
    }
}
