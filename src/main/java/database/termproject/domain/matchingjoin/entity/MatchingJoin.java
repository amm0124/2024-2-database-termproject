package database.termproject.domain.matchingjoin.entity;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.Entity;
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
    private Member member;

    @ManyToOne
    private Matching matching;

    @Builder
    public MatchingJoin(Member member, Matching matching) {
        this.member = member;
        this.matching = matching;
    }
}
