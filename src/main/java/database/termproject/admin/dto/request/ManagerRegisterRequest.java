package database.termproject.admin.dto.request;

public record ManagerRegisterRequest(
        String email,
        String password,
        String name,
        String memberAddress,
        String memberAddressDetail,
        String phoneNumber,
        String storeName,
        String address,
        String addressDetail
) {
}
