package database.termproject.domain.posting.repository;

import database.termproject.domain.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostingJPARepository extends JpaRepository<Posting, Long>{
    List<Posting> findByMemberId(Long memberId);
}




