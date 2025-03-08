package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.RepeatLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepeatRepository extends JpaRepository<RepeatLecture, Long> {
    Optional<RepeatLecture> findById(Long id);
}
