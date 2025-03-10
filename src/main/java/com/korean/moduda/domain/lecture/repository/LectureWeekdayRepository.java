package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.WeekDayLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureWeekdayRepository extends JpaRepository<WeekDayLecture, Long> {
    @Query("select l from WeekDayLecture l where l.lecture.id = :id")
    Optional<WeekDayLecture> findByLectureId(Long id);
}
