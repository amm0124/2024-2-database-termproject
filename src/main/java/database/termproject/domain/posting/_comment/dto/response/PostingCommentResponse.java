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

        if(comment.getMember().isDeleted()){
            return PostingCommentResponse.builder()
                    .commentId(comment.getId())
                    .postingId(comment.getPosting().getId())
                    .memberId(comment.getMember().getId())
                    .memberName("탈퇴한 회원")
                    .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                    .commentContent("삭제된 댓글입니다.")
                    .isDeleted(comment.isDeleted())
                    .build();
        }


        return PostingCommentResponse.builder()
                .commentId(comment.getId())
                .postingId(comment.getPosting().getId())
                .memberId(comment.getMember().getId())
                .memberName(comment.isDeleted() != true ? comment.getMember().getMemberProfile().getName() : "삭제")
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .commentContent(comment.isDeleted() != true ? comment.getContent() : "삭제된 댓글입니다.")
                .isDeleted(comment.isDeleted())
                .build();
    }

    public void addPostingCommentResponse(PostingCommentResponse postingCommentResponse) {
        this.replyList.add(postingCommentResponse);
    }

}
