package com.korean.moduda.domain.lecture.repository;

import com.korean.moduda.domain.lecture.Lecture;
import com.korean.moduda.domain.lecture.MemberLecture;
import com.korean.moduda.domain.member.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLectureRepository extends JpaRepository<MemberLecture, Long> {
    List<MemberLecture> findByMemberAndLecture_LectureDateBetween(Member member, LocalDate start, LocalDate end);

    @Query("SELECT COUNT(ml) FROM MemberLecture ml WHERE ml.member = :member AND ml.lecture.lectureDate BETWEEN :startDate AND :endDate AND ml.completed = true")
    long countCompletedByMemberAndLectureDateBetween(@Param("member") Member member, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Optional<MemberLecture> findById(Long id);

    Optional<MemberLecture> findByMemberAndLecture(Member member, Lecture lecture);
}
