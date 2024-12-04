package database.termproject.domain.facilities.entity;

import database.termproject.domain.member.entity.Member;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Table
@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Facilities extends BaseEntity {

    @OneToOne
    private Member member;

    private String storeName;

    private String address;

    private String addressDetail;

    private String phone;

    @Builder
    public Facilities(Member member, String storeName, String address, String addressDetail, String phone) {
        this.member = member;
        this.storeName = storeName;
        this.address = address;
        this.addressDetail = addressDetail;
        this.phone = phone;
    }

    public Facilities edit(String storeName, String address, String addressDetail, String phone) {

        if(storeName != null){
            this.storeName = storeName;
        }

        if(address != null){
            this.address = address;
        }

        if(addressDetail != null){
            this.addressDetail = addressDetail;
        }

        if(phone != null){
            this.phone = phone;
        }

        return this;
    }

}
