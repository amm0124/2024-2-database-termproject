package database.termproject.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String addressDetail;

    private String phoneNumber;

    @Builder
    public MemberProfile(String name, String address, String addressDetail, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.addressDetail = addressDetail;
        this.phoneNumber = phoneNumber;
    }

    public MemberProfile editMemberProfile(String name, String address, String addressDetail, String phoneNumber){
        if (name != null) {
            this.setName(name);
        }

        if (address != null) {
            this.setAddress(address);
        }

        if (addressDetail != null) {
            this.setAddressDetail(addressDetail);
        }

        if (phoneNumber != null) {
            this.setPhoneNumber(phoneNumber);
        }
        return this;
    }

}
