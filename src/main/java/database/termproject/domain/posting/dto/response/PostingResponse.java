package database.termproject.domain.posting.dto.response;

import database.termproject.domain.posting._comment.dto.response.CommentResponse;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.entity.PostingType;

import java.time.LocalDateTime;
import java.util.List;

public record PostingResponse(
        String title,
        LocalDateTime createdAt,
        //String memberName,
        String content,
        PostingType postingType
        //List<CommentResponse> commentResponse
) {


    public static PostingResponse fromEntity(Posting posting){
        return new PostingResponse(
                posting.getTitle(),
                posting.getCreatedAt(),
                //posting.getMember().getName(),
                posting.getContent(),
                posting.getPostingType()
                //posting.get
        );
    }



}