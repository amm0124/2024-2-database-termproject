package database.termproject.domain.posting._comment.service;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting._comment.CommentRepository;
import database.termproject.domain.posting._comment.dto.request.CommentReplyRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRequest;
import database.termproject.domain.posting._comment.dto.response.CommentResponse;
import database.termproject.domain.posting._comment.entity.Comment;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.service.PostingService;
import database.termproject.domain.posting.service.PostingServiceImpl;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static database.termproject.global.error.ProjectError.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostingServiceImpl postingService;

    @Transactional
    public void createComment(CommentRequest commentRequest) {

        Member member = getMember();

        Long postingId = commentRequest.postingId();
        String content = commentRequest.content();
        Long parentCommentId = commentRequest.parentCommentId(); // parentCommentId 가져오기

        Comment parentComment = null;

        if(parentCommentId != null) {
            parentComment = getCommentByCommentId(parentCommentId);
        }

        Posting posting = postingService.getPostingByPostingId(postingId);

        //자식 댓글 추가
        Comment comment = Comment.builder()
                .posting(posting)
                .member(member)
                .content(content)
                .parentComment(parentComment)
                .build();

        commentRepository.save(comment);
        //부모 comment가 있으면
        //부모에도 등록을 해줘야 한다
        if(parentComment != null) {
            parentComment.addReplies(comment);
            commentRepository.save(parentComment);
        }
    }

    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ProjectException(COMMENT_NOT_FOUND));
    }


    /*public CommentResponse commentReply(CommentReplyRequest commentReplyRequest) {

        Long postId = commentReplyRequest.postId();
        String content = commentReplyRequest.content();
        Long parentCommentId = commentReplyRequest.parentCommentId();

        Member member = getMember();

        //TODO : ~


    }*/


    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }

}
