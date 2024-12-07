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
import static database.termproject.domain.member.entity.Role.ROLE_USER;

@Entity
@Table
@Getter
//@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET is_deleted = true where id = ?")
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String password;

    @OneToOne
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    private boolean isVerify = false;

    public void deleteMember(){
        this.isDeleted = true;
    }

    @Builder
    public Member(String email, String password) {
        this.role = ROLE_ANONYMOUS;
        this.email = email;
        this.password = password;
    }


    public boolean isVerifyMember(){
        if(this.isVerify == true) return true;
        return false;
    }

    public void setMemberProfile(MemberProfile memberProfile){
        this.memberProfile = memberProfile;
    }

    public Member editMemberInfo(String password){
        if(password!=null){
            this.password = password;
        }
        return this; // 변경된 Member 객체 반환
    }

    public void convertRoleUser(){
        this.isVerify = true;
        this.role = ROLE_USER;
    }


    public void setRole(Role role){
        this.role = role;
    }
}
