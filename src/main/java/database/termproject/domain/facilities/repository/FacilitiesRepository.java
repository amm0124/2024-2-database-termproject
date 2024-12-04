package database.termproject.domain.facilities.repository;

import database.termproject.domain.facilities.entity.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities, Long> {

    Optional<Facilities> findByMemberId(Long memberId);
}
