package database.termproject.domain.posting.entity;


import database.termproject.global.entity.BaseEntity;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@Entity
@Getter
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
        this.now = 1;
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


}
