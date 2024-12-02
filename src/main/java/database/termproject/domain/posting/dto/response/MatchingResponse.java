package database.termproject.domain.posting.dto.response;

import database.termproject.domain.posting.entity.Matching;

public record MatchingResponse(Long postingId,
                               String when,
                               String place,
                               Integer now,
                               Integer limit) {
    public static MatchingResponse fromEntity(Matching matching){
        return new MatchingResponse(
                matching.getPosting().getId(),
                matching.getWhen(),
                matching.getPlace(),
                matching.getNow(),
                matching.getLimit()
        );
    }
}
