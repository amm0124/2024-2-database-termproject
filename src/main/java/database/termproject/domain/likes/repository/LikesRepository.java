package database.termproject.domain.likes.repository;


import database.termproject.domain.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByMember_IdAndPosting_Id(Long memberId, Long postingId);

    /*
    *  @Query("SELECT COUNT(l) > 0 " +
           "FROM Likes l " +
           "WHERE l.member.id = :memberId AND l.posting.id = :postingId")
    boolean existsByMemberIdAndPostingId(@Param("memberId") Long memberId, @Param("postingId") Long postingId);
    * */

}
