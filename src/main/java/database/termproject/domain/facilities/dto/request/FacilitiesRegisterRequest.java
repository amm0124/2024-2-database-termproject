package database.termproject.domain.facilities.dto.request;

import database.termproject.domain.facilities.entity.Facilities;

public record FacilitiesRegisterRequest(
        String storeName,
        String address,
        String addressDetail
) {
}
