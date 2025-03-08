package com.korean.moduda.domain.lecture.dto;

import com.korean.moduda.domain.lecture.entity.RepeatLecture;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureRepeatResponse {

    private LocalDate lectureDate;

    private String title1;
    private String content1;

    private String title2;
    private String content2;
    private String videoUrl;

    private String title3;

    private String lastDescription1;
    private String lastDescription2;

    public LectureRepeatResponse(RepeatLecture lecture) {
        this.lectureDate = lecture.getLectureDate();

        this.title1 = lecture.getTitle1();
        this.content1 = lecture.getContent1();
        this.title2 = lecture.getTitle2();
        this.content2 = lecture.getContent2();
        this.videoUrl = lecture.getVideoUrl();
        this.title3 = lecture.getTitle3();

        this.lastDescription1 = lecture.getLastDescription1();
        this.lastDescription2 = lecture.getLastDescription2();
    }
}

