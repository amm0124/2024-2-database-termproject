package database.termproject.domain.member.entity;

import jakarta.persistence.*;

@Entity
@Table
public class MemberProfile {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Member member;

    private String name;

    private String address;

    private String addressDetail;

    private String phoneNumber;


}
