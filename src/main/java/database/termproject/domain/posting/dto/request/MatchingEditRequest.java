package database.termproject.domain.posting.dto.request;

public record MatchingEditRequest(Long matchingId,
                                  String eventTime,
                                  String place,
                                  int capacity) {
}
