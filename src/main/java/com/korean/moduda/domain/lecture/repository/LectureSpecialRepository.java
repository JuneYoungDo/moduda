package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.SpecialLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureSpecialRepository extends JpaRepository<SpecialLecture, Long> {
    Optional<SpecialLecture> findById(Long id);
}
