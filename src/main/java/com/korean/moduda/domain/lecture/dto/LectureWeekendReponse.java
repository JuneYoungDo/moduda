package com.korean.moduda.domain.lecture.dto;

import com.korean.moduda.domain.lecture.entity.WeekendLecture;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureWeekendReponse {

    private LocalDate lectureDate;

    private String subject1;
    private String content1;

    private String subject2;
    private String content2;

    private String subject3;
    private String content3;

    private String subject4;
    private String content4;

    private String subject5;
    private String content5;

    private LocalDate date1;
    private String imageUrl1;
    private String audioUrl1;
    private String korean1;
    private String chinese1;

    private LocalDate date2;
    private String imageUrl2;
    private String audioUrl2;
    private String korean2;
    private String chinese2;

    private LocalDate date3;
    private String imageUrl3;
    private String audioUrl3;
    private String korean3;
    private String chinese3;

    private LocalDate date4;
    private String imageUrl4;
    private String audioUrl4;
    private String korean4;
    private String chinese4;

    private LocalDate date5;
    private String imageUrl5;
    private String audioUrl5;
    private String korean5;
    private String chinese5;

    private String lastDescription1;
    private String lastDescription2;

    public LectureWeekendReponse(WeekendLecture lecture) {
        this.lectureDate = lecture.getLectureDate();

        this.subject1 = lecture.getSubject1();
        this.content1 = lecture.getContent1();
        this.subject2 = lecture.getSubject2();
        this.content2 = lecture.getContent2();
        this.subject3 = lecture.getSubject3();
        this.content3 = lecture.getContent3();
        this.subject4 = lecture.getSubject4();
        this.content4 = lecture.getContent4();
        this.subject5 = lecture.getSubject5();
        this.content5 = lecture.getContent5();

        this.date1 = lecture.getDate1();
        this.imageUrl1 = lecture.getImageUrl1();
        this.audioUrl1 = lecture.getAudioUrl1();
        this.korean1 = lecture.getKorean1();
        this.chinese1 = lecture.getChinese1();

        this.date2 = lecture.getDate2();
        this.imageUrl2 = lecture.getImageUrl2();
        this.audioUrl2 = lecture.getAudioUrl2();
        this.korean2 = lecture.getKorean2();
        this.chinese2 = lecture.getChinese2();

        this.date3 = lecture.getDate3();
        this.imageUrl3 = lecture.getImageUrl3();
        this.audioUrl3 = lecture.getAudioUrl3();
        this.korean3 = lecture.getKorean3();
        this.chinese3 = lecture.getChinese3();

        this.date4 = lecture.getDate4();
        this.imageUrl4 = lecture.getImageUrl4();
        this.audioUrl4 = lecture.getAudioUrl4();
        this.korean4 = lecture.getKorean4();
        this.chinese4 = lecture.getChinese4();

        this.date5 = lecture.getDate5();
        this.imageUrl5 = lecture.getImageUrl5();
        this.audioUrl5 = lecture.getAudioUrl5();
        this.korean5 = lecture.getKorean5();
        this.chinese5 = lecture.getChinese5();

        this.lastDescription1 = lecture.getLastDescription1();
        this.lastDescription2 = lecture.getLastDescription2();
    }
}

