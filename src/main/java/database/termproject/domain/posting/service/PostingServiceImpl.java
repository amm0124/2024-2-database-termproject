package database.termproject.domain.posting.service;

import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.PostingJPARepository;
import database.termproject.domain.posting.dto.request.PostingRequest;
import database.termproject.domain.posting.dto.response.PostingResponse;
import database.termproject.domain.posting.entity.PostingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostingServiceImpl implements PostingService<PostingRequest> {

    private final PostingJPARepository postingJPARepository;

    @Override
    public PostingResponse createFreePosting(PostingRequest postingRequest) {
        String title = postingRequest.title();
        String content = postingRequest.content();
        PostingType postingType = PostingType.FREE;

        //TODO : Member 객체 spring context에서 가져와야 함
        Member member = Member.builder().build();

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


    /*public List<PostingResponse> getPosting(){
        //TODO : jwt에서 가져와야 함
        Long memberId = 1L;
        List<Posting> postingList = postingJPARepository.findByMemberId(memberId);
        //return PostingResponse.


    }*/

}
