package database.termproject.domain.matchingjoin.repository;

import database.termproject.domain.matchingjoin.entity.MatchingJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingJoinRepository extends JpaRepository<MatchingJoin, Long> {
}
