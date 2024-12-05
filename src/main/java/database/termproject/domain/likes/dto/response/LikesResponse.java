package database.termproject.domain.likes.dto.response;

import database.termproject.domain.likes.entity.Likes;
import database.termproject.domain.likes.entity.LikesType;

public record LikesResponse(Long postingId,
                            Long memberId,
                            LikesType likesType) {
    public static LikesResponse from(Likes likes) {
        return new LikesResponse(
                likes.getPosting().getId(),
                likes.getMember().getId(),
                likes.getLikesType()
        );
    }
}
