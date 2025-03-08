package com.korean.moduda.domain.lecture.dto;

import com.korean.moduda.domain.lecture.entity.SpecialLecture;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureSpecialResponse {

    private LocalDate lectureDate;

    private String title;
    private String content;

    private String lastDescription1;
    private String lastDescription2;

    public LectureSpecialResponse(SpecialLecture lecture) {
        this.lectureDate = lecture.getLectureDate();

        this.title = lecture.getTitle();
        this.content = lecture.getContent();

        this.lastDescription1 = lecture.getLastDescription1();
        this.lastDescription2 = lecture.getLastDescription2();
    }
}

