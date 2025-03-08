package com.korean.moduda.domain.lecture.entity;

import com.korean.moduda.domain.lecture.Lecture;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lectures_weekday")
public class WeekDayLecture extends BaseLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

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
}
