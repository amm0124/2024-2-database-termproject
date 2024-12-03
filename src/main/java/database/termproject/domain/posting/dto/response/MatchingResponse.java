package database.termproject.domain.posting.dto.response;

import database.termproject.domain.posting.entity.Matching;

public record MatchingResponse(Long postingId,
                               String eventTime,
                               String place,
                               Integer now,
                               Integer capacity) {
    public static MatchingResponse fromEntity(Matching matching){
        return new MatchingResponse(
                matching.getPosting().getId(),
                matching.getEventTime(),
                matching.getPlace(),
                matching.getNow(),
                matching.getCapacity()
        );
    }
}
