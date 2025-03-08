package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.LastLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureLastRepository extends JpaRepository<LastLecture, Long> {
    Optional<LastLecture> findById(Long id);
}
