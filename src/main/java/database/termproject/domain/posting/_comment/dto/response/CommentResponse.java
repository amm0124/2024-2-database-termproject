package database.termproject.domain.posting._comment.dto.response;

public record CommentResponse(
        Long commentId,
        Long postingId,
        Long memberId,
        Long parentCommentId,
        String commentContent,
        Boolean isDeleted
) {
}
