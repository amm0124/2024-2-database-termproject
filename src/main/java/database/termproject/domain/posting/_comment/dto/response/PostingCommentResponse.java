package database.termproject.domain.posting._comment.dto.response;

import database.termproject.domain.posting._comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostingCommentResponse {

    private Long commentId;

    private Long postingId;

    private Long memberId;

    private String memberName;

    private Long parentCommentId;

    private String commentContent;

    private Boolean isDeleted;

    private List<PostingCommentResponse> replyList = new ArrayList<>();

    @Builder
    public PostingCommentResponse(Long commentId, Long postingId, Long memberId, Long parentCommentId, String commentContent, Boolean isDeleted, String memberName) {
        this.commentId = commentId;
        this.postingId = postingId;
        this.memberId = memberId;
        this.parentCommentId = parentCommentId;
        this.commentContent = commentContent;
        this.isDeleted = isDeleted;
        this.memberName = memberName;
    }

    public static PostingCommentResponse fromEntity(Comment comment){
        return PostingCommentResponse.builder()
                .commentId(comment.getId())
                .postingId(comment.getPosting().getId())
                .memberId(comment.getMember().getId())
                .memberName(comment.getMember().getMemberProfile().getName())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .commentContent(comment.getContent())
                .isDeleted(comment.isDeleted())
                .build();
    }

    public void addPostingCommentResponse(PostingCommentResponse postingCommentResponse) {
        this.replyList.add(postingCommentResponse);
    }
}
