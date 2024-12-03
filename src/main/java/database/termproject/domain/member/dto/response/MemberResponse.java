package database.termproject.domain.member.dto.response;

import database.termproject.domain.member.entity.Member;
import database.termproject.domain.member.entity.Role;

public record MemberResponse(
        String email,
        Role role,
        boolean isDeleted,
        String name,
        String address,
        String addressDetail,
        String phoneNumber
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getEmail(),
                member.getRole(),
                member.isDeleted(),
                member.getMemberProfile().getName(),
                member.getMemberProfile().getAddress(),
                member.getMemberProfile().getAddressDetail(),
                member.getMemberProfile().getPhoneNumber()
        );
    }
}
