package database.termproject.domain.posting.dto.request;

public record MatchingTournamentPostingRequest(
        String title,
        String content,
        String game,
        String when,
        String place,
        Integer limit
) {
}
