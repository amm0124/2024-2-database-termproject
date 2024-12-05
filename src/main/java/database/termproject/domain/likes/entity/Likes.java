package database.termproject.domain.likes.entity;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Table(name = "likes")
@Getter
@Entity
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;


    @Enumerated(EnumType.STRING)
    private LikesType likesType;

    @Builder
    public Likes(Member member, Posting posting, LikesType likesType) {
        this.member = member;
        this.posting = posting;
        this.likesType = likesType;
    }

    public Integer getLikesCount(){
        return this.posting.getLikesCount();
    }


    public void addLikes(){
        this.posting.addLikeCounts();
    }

    public void subtractLikes(){
        this.posting.subtractLikesCount();
    }
}
