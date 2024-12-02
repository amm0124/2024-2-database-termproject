package database.termproject.domain.posting.dto.response;

import database.termproject.domain.posting._comment.dto.response.PostingCommentResponse;

import java.util.List;

public record PostingDetailResponse(
        PostingResponse postingResponse,
        List<PostingCommentResponse> postingCommentResponseList,
        MatchingResponse matchingResponse
) {

    public static PostingDetailResponse from(PostingResponse postingResponse,
                                              List<PostingCommentResponse> postingCommentResponseList,
                                              MatchingResponse matchingResponse) {
        return new PostingDetailResponse(
                postingResponse,
                postingCommentResponseList,
                matchingResponse
        );
    }

}