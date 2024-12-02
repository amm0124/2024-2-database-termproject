package database.termproject.domain.posting.repository;

import database.termproject.domain.posting.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findByPostingId(Long postingId);
}
