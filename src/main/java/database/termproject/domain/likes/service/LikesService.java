package database.termproject.domain.likes.service;


import database.termproject.domain.likes.dto.request.LikeRequest;
import database.termproject.domain.likes.dto.response.LikesResponse;
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

import java.util.List;
import java.util.stream.Collectors;

import static database.termproject.global.error.ProjectError.LIKE_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostingServiceImpl postingService;

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

    public List<LikesResponse> getLikseResponse(Long postingId) {
        List<Likes> likesList = likesRepository.findByPosting_Id(postingId);
        return likesList.stream().map(LikesResponse::from)
                .collect(Collectors.toList());
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }

}
