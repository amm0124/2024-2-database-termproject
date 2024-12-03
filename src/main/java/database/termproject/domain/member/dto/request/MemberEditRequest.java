package database.termproject.domain.member.dto.request;

public record MemberEditRequest(String name,
                                String password,
                                String address,
                                String addressDetail,
                                String phoneNumber) {
}
