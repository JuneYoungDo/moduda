package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.SpecialLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureSpecialRepository extends JpaRepository<SpecialLecture, Long> {
    @Query("select l from SpecialLecture l where l.lecture.id = :id")
    Optional<SpecialLecture> findByLectureId(Long id);
}
