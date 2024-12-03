package database.termproject.domain.posting.service;

import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting._comment.dto.response.PostingCommentResponse;
import database.termproject.domain.posting._comment.service.CommentService;
import database.termproject.domain.posting.dto.request.MatchingTournamentPostingRequest;
import database.termproject.domain.posting.dto.request.PostingDeleteRequest;
import database.termproject.domain.posting.dto.response.MatchingResponse;
import database.termproject.domain.posting.dto.response.PostingDetailResponse;
import database.termproject.domain.posting.dto.response.PostingResponse;
import database.termproject.domain.posting.entity.Matching;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.MatchingRepository;
import database.termproject.domain.posting.repository.PostingJPARepository;
import database.termproject.domain.posting.dto.request.PostingRequest;
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
import java.util.stream.Collectors;

import static database.termproject.global.error.ProjectError.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostingServiceImpl {

    private final PostingJPARepository postingJPARepository;
    private final CommentService commentService;
    private final MatchingService matchingService;

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
    public PostingDetailResponse createMatchingTournamentPosting(MatchingTournamentPostingRequest matchingTournamentPostingRequest, PostingType postingType){
        PostingRequest postingRequest = new PostingRequest(
                matchingTournamentPostingRequest.title(),
                matchingTournamentPostingRequest.game(),
                matchingTournamentPostingRequest.content()
        );

        Posting posting = createPosting(postingRequest, postingType);
        Matching matching = matchingService.save(posting, matchingTournamentPostingRequest);

        return PostingDetailResponse.from(PostingResponse.fromEntity(posting),
                null,
                MatchingResponse.fromEntity(matching));
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
            matchingResponse = MatchingResponse.fromEntity(matching.get());
        }

        return PostingDetailResponse.from(postingResponse, postingCommentResponseList, matchingResponse);
    }

    @Transactional
    public void deletePosting(PostingDeleteRequest postingDeleteRequest){
        Long postingId = postingDeleteRequest.postingId();
        Posting posting = getPostingByPostingId(postingId);
        posting.softDelete();
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

}
