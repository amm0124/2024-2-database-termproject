package database.termproject.domain.posting._comment.dto.request;

public record CommentReplyRequest(
        Long postId,
        Long parentCommentId,
        String content
) {
}
