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

import static database.termproject.global.error.ProjectError.MATCHING_JOIN_CANCELLED_NOT_ALLOWED;
import static database.termproject.global.error.ProjectError.MATCHING_MISMATCHING;


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
        Integer temp = this.count - count; //Matching의 변경 수량
        this.count = count;
        
        if(temp > 0){ //매칭 인원 감소 -> now 인원 감소
            this.getMatching().subtractNow(temp);
        }else if(temp < 0){ //매칭 인원 증가 -> now 인원 증가
            this.getMatching().addCount(temp);
        }
        if(this.count == 0){
            this.isDeleted = true;
        }

    }

    //matching join validate 1. posting
    public boolean validate(Long memberId){

        // 작성자 == 나
        if(this.matching.getPosting().getMember().getId() == memberId){
            throw new ProjectException(MATCHING_JOIN_CANCELLED_NOT_ALLOWED);
        }

        if(memberId != member.getId()){
            throw new ProjectException(MATCHING_MISMATCHING);
        }
        return true;
    }
    
}
