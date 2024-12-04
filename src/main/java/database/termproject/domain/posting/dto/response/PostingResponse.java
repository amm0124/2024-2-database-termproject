package database.termproject.domain.posting.dto.response;

import database.termproject.domain.member.dto.response.MemberResponse;
import database.termproject.domain.posting._comment.dto.response.PostingCommentResponse;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.entity.PostingType;

import java.time.LocalDateTime;
import java.util.List;

public record PostingResponse(
        Long postingId,
        String title,
        String game,
        LocalDateTime createdAt,
        MemberResponse memberResponse,
        String content,
        PostingType postingType
) {
    public static PostingResponse fromEntity(Posting posting){
        return new PostingResponse(
                posting.getId(),
                posting.getTitle(),
                posting.getGame(),
                posting.getCreatedAt(),
                MemberResponse.from(posting.getMember()),
                posting.getContent(),
                posting.getPostingType()
        );
    }




}
