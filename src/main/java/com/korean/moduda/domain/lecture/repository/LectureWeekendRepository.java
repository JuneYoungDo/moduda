package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.WeekendLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureWeekendRepository extends JpaRepository<WeekendLecture, Long> {
    Optional<WeekendLecture> findById(Long id);
}
