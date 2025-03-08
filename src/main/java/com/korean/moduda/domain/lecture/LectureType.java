package com.korean.moduda.domain.lecture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LectureType {
    WEEKDAY, WEEKEND, SPECIAL_DAY, LAST_DAY, REPEAT_DAY
}

