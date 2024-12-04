package database.termproject.domain.posting.entity;


import database.termproject.global.entity.BaseEntity;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import static database.termproject.global.error.ProjectError.MATCHING_CAPACITY_EXCEEDED;
import static database.termproject.global.error.ProjectError.MATCHING_JOIN_CANCELLED_NOT_ALLOWED;

@Table
@Entity
@Getter
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matching extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    private String eventTime;

    private String place;

    private Integer now;

    private Integer capacity;

    private boolean isFull = false;

    @Builder
    public Matching(Posting posting, String eventTime, String place, Integer capacity) {
        this.posting = posting;
        this.eventTime = eventTime;
        this.place = place;
        this.now = 0;
        this.capacity = capacity;
    }

    public void setFull(boolean status){
        this.isFull = status;
    }


    public Matching updateMatching(String eventTime, String place, Integer capacity){
        if(eventTime != null){
            this.eventTime = eventTime;
        }

        if(place != null){
            this.place = place;
        }

        if(capacity != null){
            if(now > capacity){
                throw new ProjectException(ProjectError.MATCHING_UPDATE_BAD_REQUEST);
            }
            this.capacity = capacity;
        }

        return this;
    }

    public void addNow(Integer count){
        if (this.now + count > capacity) {
            throw new ProjectException(MATCHING_CAPACITY_EXCEEDED);
        }
        this.now = this.now + count;
    }

    public void addCount(Integer count){
        this.now += count;
    }

    // 사람 증가로 인한 now 인원 증가
    public void subtractNow(Integer count){
        if(this.now - count <0){
            throw new ProjectException(MATCHING_CAPACITY_EXCEEDED);
        }
        this.now = this.now - count;
    }

    public boolean validate(Long memberId){
        if(this.getPosting().getMember().getId() == memberId){
            return true;
        }
        return false;
    }

}
