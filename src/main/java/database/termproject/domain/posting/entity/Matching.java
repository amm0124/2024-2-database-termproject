package database.termproject.domain.posting.entity;


import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private Posting posting;

    private String when;

    private String place;

    private Integer now;

    private Integer limit;

    @Builder
    public Matching(Posting posting, String when, String place, Integer limit) {
        this.posting = posting;
        this.when = when;
        this.place = place;
        this.now = 1;
        this.limit = limit;
    }

}
