package com.korean.moduda.domain.lecture.entity;

import com.korean.moduda.domain.lecture.Lecture;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lectures_weekend")
public class WeekendLecture extends BaseLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

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
}
