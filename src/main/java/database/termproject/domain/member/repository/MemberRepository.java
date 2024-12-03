package database.termproject.domain.member.repository;

import database.termproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    @Query("SELECT m FROM Member m JOIN FETCH m.memberProfile WHERE m.id = :id")
    Optional<Member> findByIdWithProfile(@Param("id") Long id);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberProfile WHERE m.email = :email")
    Optional<Member> findByEmailWithProfile(@Param("email") String email);


}
