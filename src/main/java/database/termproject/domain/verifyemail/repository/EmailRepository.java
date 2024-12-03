package database.termproject.domain.verifyemail.repository;

import database.termproject.domain.verifyemail.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailVerification,Long> {
    Optional<EmailVerification> findByMemberIdAndCode(Long memberId, String code);
}
