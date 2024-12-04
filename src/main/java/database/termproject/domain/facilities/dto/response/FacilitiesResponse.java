package database.termproject.domain.facilities.dto.response;

import database.termproject.domain.facilities.entity.Facilities;

public record FacilitiesResponse(
        Long facilitiesId,
        String storeName,
        String address,
        String addressDetail,
        String phone
) {
    public static FacilitiesResponse from(Facilities facilities){
        return new FacilitiesResponse(
                facilities.getId(),
                facilities.getStoreName(),
                facilities.getAddress(),
                facilities.getAddressDetail(),
                facilities.getPhone()
        );
    }

}
