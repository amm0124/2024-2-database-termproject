package database.termproject.domain.posting._comment.entity;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id", nullable = false)
    private Posting posting;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private int depth = 1;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();  // 대댓글 목록

    @Builder
    public Comment(Member member, Posting posting, String content, Comment parentComment) {
        this.member = member;
        this.posting = posting;
        this.content = content;
        this.parentComment = parentComment;
        this.isDeleted = false;
        this.setCommentDepth();
    }

    public void addParentComment(Comment parentComment){
        if(parentComment != null){
            this.parentComment.getReplies().add(this);
        }
    }

    public void addReplies(Comment replies){
        this.replies.add(replies);
    }

    public void setCommentDepth(){
        if(this.parentComment == null){
            this.depth=1;
        }else{
            this.depth = this.parentComment.getDepth()+1;
        }
    }

    public Long getPostingId(){
        return this.posting.getId();
    }

    public Long getParentCommentId(){
        if(this.parentComment == null){
            return null;
        }
        return this.parentComment.getId();
    }

}
