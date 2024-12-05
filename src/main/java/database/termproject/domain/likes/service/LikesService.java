package database.termproject.domain.likes.service;


import database.termproject.domain.likes.dto.request.LikeRequest;
import database.termproject.domain.likes.entity.Likes;
import database.termproject.domain.likes.entity.LikesType;
import database.termproject.domain.likes.repository.LikesRepository;
import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.service.PostingServiceImpl;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static database.termproject.domain.likes.entity.LikesType.DISLIKES;
import static database.termproject.domain.likes.entity.LikesType.LIKES;
import static database.termproject.global.error.ProjectError.LIKE_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostingServiceImpl postingService;

    
    //todo : controller에서 type을 넣어 주어 한 메서드로 변경해버리자
    @Transactional
    public void addLikes(LikeRequest likeRequest, LikesType likesType){
        Long postingId = likeRequest.postingId();
        Member member = getMember();
        Long memberId = member.getId();

        if(likesRepository.existsByMember_IdAndPosting_Id(memberId, postingId)){
            throw new ProjectException(LIKE_ALREADY_EXISTS);
        }

        Posting posting = postingService.getPostingByPostingId(postingId);

        Likes likes = Likes.builder()
                .member(member)
                .posting(posting)
                .likesType(likesType)
                .build();

        likesRepository.save(likes);

        posting.addLikeCounts();
    }


    public Likes findByLikeId(Long likesId){
        Likes likes = likesRepository.findById(likesId)
                .orElseThrow(() -> new ProjectException(LIKE_ALREADY_EXISTS));
        return likes;
    }


    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }

}
