package database.termproject.domain.posting.dto.response;

import database.termproject.domain.matchingjoin.dto.response.MatchingJoinResponse;
import database.termproject.domain.posting.entity.Matching;

import java.util.ArrayList;
import java.util.List;

public record MatchingResponse(Long matchingId, Long postingId,
                               String eventTime,
                               String place,
                               Integer now,
                               Integer capacity,
                               List<MatchingJoinResponse> matchingJoinResponseList) {
    public static MatchingResponse fromEntity(Matching matching, List<MatchingJoinResponse> matchingJoinList){

        Integer now = matchingJoinList.stream()
                .mapToInt(MatchingJoinResponse::count)
                .sum();

        return new MatchingResponse(
                matching.getId(),
                matching.getPosting().getId(),
                matching.getEventTime(),
                matching.getPlace(),
                now,
                matching.getCapacity(),
                matchingJoinList
        );
    }
}
