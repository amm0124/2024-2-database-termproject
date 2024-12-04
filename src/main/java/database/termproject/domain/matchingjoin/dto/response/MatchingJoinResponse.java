package database.termproject.domain.matchingjoin.dto.response;

import database.termproject.domain.matchingjoin.entity.MatchingJoin;

public record MatchingJoinResponse(
        Long memberId,
        String memberName,
        Integer count
) {

    public static MatchingJoinResponse from(MatchingJoin matchingJoin) {
        return new MatchingJoinResponse(
                matchingJoin.getMemberId(),
                matchingJoin.getMemberName(),
                matchingJoin.getCount()
        );
    }

}