package database.termproject.domain.posting.dto.response;

public record MatchingPostingResponse(
        Long postId,
        Long matchingId,
        String title,
        String content,
        String game,
        String when,
        Integer now,
        String place
        //,List<CommentResonse> commentList
) {
}
