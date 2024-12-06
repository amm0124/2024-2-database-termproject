package database.termproject.domain.posting._comment;

import database.termproject.domain.posting._comment.entity.Comment;
import database.termproject.domain.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPosting_IdOrderByDepth(Long postingId);

    @Query(value = "SELECT * FROM comment WHERE is_deleted = true AND id = :commentId", nativeQuery = true)
    Optional<Comment> findDeletedCommentById(@Param("commentId") Long commentId);

    Optional<Comment> findByPosting_IdAndParentComment_Id(Long postingId, Long parentCommentId);
}
