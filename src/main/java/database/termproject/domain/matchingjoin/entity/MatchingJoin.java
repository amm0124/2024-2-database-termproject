package database.termproject.domain.matchingjoin.entity;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingJoin extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "matching_id")
    private Matching matching;

    private Integer count;

    @Builder
    public MatchingJoin(Member member, Matching matching, Integer count) {
        this.member = member;
        this.matching = matching;
        this.count = count;
    }

    public Long getMemberId(){
        return member.getId();
    }

    public String getMemberName(){
        return member.getMemberProfile().getName();
    }

}
