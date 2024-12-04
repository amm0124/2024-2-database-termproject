package database.termproject.domain.posting.service;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import database.termproject.domain.facilities.service.FacilitiesService;
import database.termproject.domain.matchingjoin.dto.request.MatchingJoinRequest;
import database.termproject.domain.matchingjoin.dto.response.MatchingJoinResponse;
import database.termproject.domain.matchingjoin.service.MatchingJoinService;
import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting._comment.dto.response.PostingCommentResponse;
import database.termproject.domain.posting._comment.service.CommentService;
import database.termproject.domain.posting.dto.request.*;
import database.termproject.domain.posting.dto.response.MatchingResponse;
import database.termproject.domain.posting.dto.response.PostingDetailResponse;
import database.termproject.domain.posting.dto.response.PostingResponse;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.PostingJPARepository;
import database.termproject.domain.posting.entity.PostingType;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static database.termproject.global.error.ProjectError.POSTING_DELETE_REQUEST_MISMATCHING;
import static database.termproject.global.error.ProjectError.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostingServiceImpl {

    private final PostingJPARepository postingJPARepository;
    private final CommentService commentService;
    private final MatchingService matchingService;
    private final FacilitiesService facilitiesService;
    private final MatchingJoinService matchingJoinService;

    //free, tip
    public Posting createPosting(PostingRequest postingRequest, PostingType postingType) {
        String title = postingRequest.title();
        String game = postingRequest.game();
        String content = postingRequest.content();

        Member member = getMember();

        Posting posting = Posting
                .builder()
                .member(member)
                .title(title)
                .game(game)
                .content(content)
                .postingType(postingType)
                .build();

        postingJPARepository.save(posting);
        System.out.println("포스팅완료`~~");
        return posting;
    }

    //matching, 대회
    @Transactional
    public PostingDetailResponse createMatchingPosting(MatchingTournamentPostingRequest matchingTournamentPostingRequest, PostingType postingType){
        PostingRequest postingRequest = new PostingRequest(
                matchingTournamentPostingRequest.title(),
                matchingTournamentPostingRequest.game(),
                matchingTournamentPostingRequest.content()
        );

        Posting posting = createPosting(postingRequest, postingType);
        
        String place = matchingTournamentPostingRequest.place();
        if(place == null){ 
            place = facilitiesService.getFacilities().storeName();
        }
        //else : 즉 대회가 아니면 자기 자신 참여해야 함

        Matching matching = matchingService.save(posting,
                matchingTournamentPostingRequest.when(),
                place,
                matchingTournamentPostingRequest.limit()
        );

        if(postingType == PostingType.MATCHING){
            matchingJoinService.matchingJoin(new MatchingJoinRequest(matching.getId(), 1));
        }

        List<MatchingJoinResponse> matchingJoinList = matchingJoinService.getMatchingJoins(matching.getId());

        return PostingDetailResponse.from(PostingResponse.fromEntity(posting),
                null,
                MatchingResponse.fromEntity(matching, matchingJoinList));
    }

    //Type별로 전체 postingdetailresponse 가져 옴
    public List<PostingDetailResponse> getPosting(PostingType postingType){
        List<Posting> postingList = postingJPARepository.findByPostingType(postingType);
        return postingList.stream()
                .map(posting -> {
                    Long postingId = posting.getId();
                    return getPostingDetailResponse(postingId);
                })
                .toList();
    }

    //1개 Posting과 모든 comment와 matching
    public PostingDetailResponse getPostingDetailResponse(Long postingId){
        Posting posting = getPostingByPostingId(postingId);

        PostingResponse postingResponse = PostingResponse.fromEntity(posting);
        List<PostingCommentResponse> postingCommentResponseList = commentService.getCommentResponse(postingId);
        Optional<Matching> matching = matchingService.findByPostingId(postingId);
        MatchingResponse matchingResponse = null;
        if(matching.isPresent()){
            List<MatchingJoinResponse> matchingJoinList = matchingJoinService.getMatchingJoins(matching.get().getId());
            matchingResponse = MatchingResponse.fromEntity(matching.get(), matchingJoinList);
        }

        return PostingDetailResponse.from(postingResponse, postingCommentResponseList, matchingResponse);
    }

    @Transactional
    public void deletePosting(PostingDeleteRequest postingDeleteRequest){
        Long postingId = postingDeleteRequest.postingId();
        Posting posting = getPostingByPostingId(postingId);

        if(validatingMyPosting(postingId)){
            posting.softDelete();
        }
    }

    @Transactional
    public PostingDetailResponse updatePosting(UpdatePostingRequest updatePostingRequest){
        Long postingId = updatePostingRequest.postingId();
        Posting posting = getPostingByPostingId(postingId);

        if(validatingMyPosting(postingId)){
            posting.updatePosting(updatePostingRequest.title(),
                    updatePostingRequest.game(),
                    updatePostingRequest.content());
        }
        
        return getPostingDetailResponse(postingId);
    }

    @Transactional
    public MatchingResponse updateMatching(MatchingEditRequest matchingEditRequest){
        Matching matching = matchingService.update(matchingEditRequest);
        Long matchingId = matching.getId();
        List<MatchingJoinResponse> matchingJoinList = matchingJoinService.getMatchingJoins(matching.getId());
        return MatchingResponse.fromEntity(matching, matchingJoinList);
    }


    public Posting getPostingByPostingId(Long postingId) {
        return postingJPARepository.findById(postingId)
                .orElseThrow(() -> new ProjectException(POSTING_NOT_FOUND));
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }

    private boolean validatingMyPosting(Long postingId){
        Member member = getMember();
        Posting posting = getPostingByPostingId(postingId);
        if(posting.getMember().getId() != member.getId()){
            throw new ProjectException(POSTING_DELETE_REQUEST_MISMATCHING);
        }
        return true;
    }

}
