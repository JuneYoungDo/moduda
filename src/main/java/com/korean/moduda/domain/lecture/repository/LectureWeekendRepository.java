package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.WeekendLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureWeekendRepository extends JpaRepository<WeekendLecture, Long> {
    @Query("select l from WeekendLecture l where l.lecture.id = :id")
    Optional<WeekendLecture> findByLectureId(Long id);
}
