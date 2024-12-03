package database.termproject.domain.verifyemail.entity;


import database.termproject.domain.member.entity.Member;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 기본 생성자
public class EmailVerification extends BaseEntity {

    @OneToOne //fk
    private Member member;

    private String code;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    public EmailVerification(Member member , String code) {
        this.member = member;
        this.code = code;
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }

}
