package database.termproject.domain.posting.dto.request;

import database.termproject.domain.posting.entity.PostingType;

public record PostingRequest(
        String title,
        String content) {
}
