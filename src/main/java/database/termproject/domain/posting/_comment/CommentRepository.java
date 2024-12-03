package database.termproject.domain.posting._comment;

import database.termproject.domain.posting._comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPosting_IdOrderByDepth(Long postingId);

    Optional<Comment> findByPosting_IdAndParentComment_Id(Long postingId, Long parentCommentId);
}
