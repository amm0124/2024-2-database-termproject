package database.termproject.domain.posting._comment.entity;


import database.termproject.domain.member.entity.Member;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table
@Getter
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
*/
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private boolean isDeleted;

/*
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;
*/



}
