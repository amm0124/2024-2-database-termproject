package database.termproject.domain.matchingjoin.repository;

import database.termproject.domain.matchingjoin.entity.MatchingJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingJoinRepository extends JpaRepository<MatchingJoin, Long> {
    boolean existsByMatching_IdAndMember_Id(Long matchingId, Long memberId);
    List<MatchingJoin> findByMatchingId(Long matchingId);
}
