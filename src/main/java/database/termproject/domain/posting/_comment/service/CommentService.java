package database.termproject.domain.posting._comment.service;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting._comment.CommentRepository;
import database.termproject.domain.posting._comment.dto.request.CommentReplyRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRequest;
import database.termproject.domain.posting._comment.dto.response.PostingCommentResponse;
import database.termproject.domain.posting._comment.entity.Comment;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.PostingJPARepository;
import database.termproject.domain.posting.repository.PostingRepository;
import database.termproject.domain.posting.service.PostingServiceImpl;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database.termproject.global.error.ProjectError.COMMENT_NOT_FOUND;
import static database.termproject.global.error.ProjectError.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostingJPARepository postingRepository;

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

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new ProjectException(POSTING_NOT_FOUND));

        //자식 댓글 추가
        Comment comment = Comment.builder()
                .posting(posting)
                .member(member)
                .content(content)
                .parentComment(parentComment)
                .build();

        comment.setCommentDepth();

        commentRepository.save(comment);

        if(parentComment != null) {
            parentComment.addReplies(comment);
            commentRepository.save(parentComment);
        }
    }

    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ProjectException(COMMENT_NOT_FOUND));
    }


    public List<PostingCommentResponse> getCommentResponse(Long postingId) {
        List<PostingCommentResponse> postingCommentResponseList = new ArrayList<>();
        List<Comment> commentList = commentRepository.findByPostingId(postingId);
        Map<Long, PostingCommentResponse> postingCommentResponseMap = new HashMap<>();

        for (Comment comment : commentList) {
            if (comment.getDepth() == 1) {
                PostingCommentResponse postingCommentResponse = PostingCommentResponse.fromEntity(comment);
                postingCommentResponseList.add(postingCommentResponse);
                postingCommentResponseMap.put(comment.getId(), postingCommentResponse);
            } else {
                Long parentCommentId = comment.getParentComment().getId();
                PostingCommentResponse parentCommentResponse = postingCommentResponseMap.get(parentCommentId);
                parentCommentResponse.addPostingCommentResponse(
                        PostingCommentResponse.fromEntity(comment)
                );
            }
        }
        return postingCommentResponseList;
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }

}
