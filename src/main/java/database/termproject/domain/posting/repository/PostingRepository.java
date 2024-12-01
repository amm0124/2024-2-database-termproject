package database.termproject.domain.posting.repository;

import database.termproject.domain.posting.entity.Posting;

import java.util.List;

public interface PostingRepository {
    List<Posting> findByMemberId(Long memberId);
    void save(Posting posting);
}
