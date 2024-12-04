package database.termproject.domain.posting.dto.request;

public record UpdatePostingRequest(Long postingId, String title, String game, String content) {

}
