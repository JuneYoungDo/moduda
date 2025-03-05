package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.Lecture;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Long countByLectureDateBetween(LocalDate start, LocalDate end);

    Optional<Lecture> findById(Long id);
}
