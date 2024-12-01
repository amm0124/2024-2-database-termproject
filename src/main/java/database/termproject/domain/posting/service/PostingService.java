package database.termproject.domain.posting.service;

import database.termproject.domain.posting.dto.response.PostingResponse;

public interface PostingService<P> {
    PostingResponse createFreePosting(P p);
    //PostingResponse createTipPosting(P p);
    //PostingResponse createMatchingPosting(P p);
}
