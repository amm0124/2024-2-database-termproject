package database.termproject.domain.posting.service;

import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.PostingJPARepository;
import database.termproject.domain.posting.dto.request.PostingRequest;
import database.termproject.domain.posting.dto.response.PostingResponse;
import database.termproject.domain.posting.entity.PostingType;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static database.termproject.global.error.ProjectError.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostingServiceImpl implements PostingService<PostingRequest> {

    private final PostingJPARepository postingJPARepository;

    @Override
    public PostingResponse createFreePosting(PostingRequest postingRequest) {
        String title = postingRequest.title();
        String content = postingRequest.content();
        PostingType postingType = PostingType.FREE;

        Member member = getMember();

        Posting posting = Posting
                .builder()
                .member(member)
                .title(title)
                .content(content)
                .postingType(postingType)
                .build();

        postingJPARepository.save(posting);
        return PostingResponse.fromEntity(posting);
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



    /*public List<PostingResponse> getPosting(){
        //TODO : jwt에서 가져와야 함
        Long memberId = 1L;
        List<Posting> postingList = postingJPARepository.findByMemberId(memberId);
        //return PostingResponse.


    }*/

}
