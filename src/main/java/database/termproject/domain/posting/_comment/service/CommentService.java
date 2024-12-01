package database.termproject.domain.posting._comment.service;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting._comment.CommentRepository;
import database.termproject.domain.posting._comment.dto.request.CommentReplyRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRequest;
import database.termproject.domain.posting._comment.dto.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponse createComment(CommentRequest commentRequest) {

        Member member = getMember();

        Long postId = commentRequest.postId();
        String content = commentRequest.content();

        return null;
    }

    /*public CommentResponse commentReply(CommentReplyRequest commentReplyRequest) {

        Long postId = commentReplyRequest.postId();
        String content = commentReplyRequest.content();
        Long parentCommentId = commentReplyRequest.parentCommentId();

        Member member = getMember();

        //TODO : ~


    }*/


    private Member getMember() {
        // TODO : member를 security context에서 가져와야 함
        // Member member = new Member();

        //TODO Long memberId = member.getId();
        Long memberId = 1L;
        return null;
    }

}
