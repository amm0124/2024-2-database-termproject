package database.termproject.domain.matchingjoin.entity;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.global.entity.BaseEntity;
import database.termproject.global.error.ProjectException;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static database.termproject.global.error.ProjectError.*;


@Table
@Entity
@Getter
@SQLRestriction("is_deleted = false")
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

    //count로 변경함
    public void calculate(Integer count){
        //감소

        if(count < 0) {
            throw new ProjectException(MATCHING_BAD_REQUEST);
        }

        Integer temp = this.count - count; //Matching의 감소 수량

        this.count = count;

        if(temp !=0){
            this.getMatching().subtractNow(temp);
        }

        if(this.count == 0){
            this.isDeleted = true;
        }

    }

    //matching join validate 1. posting
    public boolean validateMyRequest(Long loginMemberId){

        if(this.member.getId() != loginMemberId){
            throw new ProjectException(MATCHING_MISMATCHING);
        }
        return true;

    }

    public void softDelete(){
        this.isDeleted = true;
    }


    public void change(Integer count){
        if(this.count==count){
            this.isDeleted = true;
        }
        this.count = count;
    }

}
