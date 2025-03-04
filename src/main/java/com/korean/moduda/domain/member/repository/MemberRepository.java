package com.korean.moduda.domain.member.repository;

import com.korean.moduda.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(value = "select m from Member m where m.id = :id and m.deleted = false")
    Optional<Member> findByIdWithDeleted(Long id);

    Optional<Member> findByEmail(String email);

    @Query(value = "select m from Member m where m.deleted = false")
    List<Member> findAllByDeleted();
}
