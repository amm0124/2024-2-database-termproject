package database.termproject.domain.member.dto.request;

public record MemberSignUpRequestDto(
        String email,
        String password,
        String name,
        String address,
        String addressDetail,
        String phoneNumber
) {
}
