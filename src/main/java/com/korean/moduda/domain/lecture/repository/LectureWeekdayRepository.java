package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.WeekDayLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureWeekdayRepository extends JpaRepository<WeekDayLecture, Long> {
    Optional<WeekDayLecture> findById(Long id);
}
