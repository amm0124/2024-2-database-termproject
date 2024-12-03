package database.termproject.domain.posting._comment.dto.request;

public record CommentRequest(
        Long postingId,
        String content) {
}
