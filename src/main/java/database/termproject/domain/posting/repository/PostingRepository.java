package database.termproject.domain.posting.repository;

import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.entity.PostingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostingRepository extends JpaRepository<Posting, Long>{
    List<Posting> findByMemberId(Long memberId);

    @Query("SELECT p FROM Posting p WHERE p.postingType = :postingType ORDER BY p.likesCount DESC")
    List<Posting> findByPostingTypeOrderedByLikesCountDesc(@Param("postingType") PostingType postingType);

    //List<Posting> findByPostingType(PostingType postingType);

    @Query(value = "SELECT * FROM posting WHERE is_deleted = true AND id = :postingId", nativeQuery = true)
    Optional<Posting> findDeletedPostingsById(@Param("postingId") Long postingId);
}





