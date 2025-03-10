package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.entity.LastLecture;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureLastRepository extends JpaRepository<LastLecture, Long> {
    @Query("select l from LastLecture l where l.lecture.id = :id")
    Optional<LastLecture> findByLectureId(Long id);
}
