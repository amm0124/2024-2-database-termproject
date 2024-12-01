package database.termproject.domain.member.entity;

import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static database.termproject.domain.member.entity.Role.ROLE_ANONYMOUS;

@Entity
@Table
@Getter
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE artist SET is_deleted = true where id = ?")
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String password;

    @OneToOne
    private MemberProfile memberProfile;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void deleteMember(){
        this.isDeleted = true;
    }

    @Builder
    public Member(String email, String password) {
        this.role = ROLE_ANONYMOUS;
        this.email = email;
        this.password = password;
    }

}
