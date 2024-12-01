package database.termproject.domain.posting.service;

import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.dto.request.MatchingTournamentPostingRequest;
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

import java.util.List;
import java.util.stream.Collectors;

import static database.termproject.global.error.ProjectError.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostingServiceImpl {

    private final PostingJPARepository postingJPARepository;
    private final MatchingRepository matchingRepository;

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
        return posting;
    }

    //matching, 대회
    public void createMatchingTournamentPosting(MatchingTournamentPostingRequest matchingTournamentPostingRequest, PostingType postingType){
        PostingRequest postingRequest = new PostingRequest(
                matchingTournamentPostingRequest.title(),
                matchingTournamentPostingRequest.game(),
                matchingTournamentPostingRequest.content()
        );
        Posting posting = createPosting(postingRequest, postingType);

        Matching matching = Matching.builder()
                .posting(posting)
                .when(matchingTournamentPostingRequest.when())
                .place(matchingTournamentPostingRequest.place())
                .limit(matchingTournamentPostingRequest.limit())
                .build();
        matchingRepository.save(matching);
    }


    //GET
    public List<PostingResponse> getPosting(PostingType postingType){
        List<Posting> postingList =  postingJPARepository.findByPostingType(postingType);
        return postingList.stream()
                .map(PostingResponse::fromEntity)
                .collect(Collectors.toList())
                ;
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
