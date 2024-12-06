package database.termproject.domain.posting._comment.service;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.posting._comment.CommentRepository;
import database.termproject.domain.posting._comment.dto.request.CommentEditRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRemoveRequest;
import database.termproject.domain.posting._comment.dto.request.CommentReplyRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRequest;
import database.termproject.domain.posting._comment.dto.response.PostingCommentResponse;
import database.termproject.domain.posting._comment.entity.Comment;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.repository.PostingRepository;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database.termproject.global.error.ProjectError.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;

    @Transactional
    public PostingCommentResponse addComment(CommentRequest commentRequest) {

        Member member = getMember();

        Long postingId = commentRequest.postingId();
        String content = commentRequest.content();

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new ProjectException(POSTING_NOT_FOUND));

        Comment parentComment = null;

        Comment comment = Comment.builder()
                .posting(posting)
                .member(member)
                .content(content)
                .parentComment(parentComment)
                .build();

        commentRepository.save(comment);

        PostingCommentResponse postingCommentResponse = PostingCommentResponse.fromEntity(comment);
        return postingCommentResponse;
    }


    @Transactional
    public PostingCommentResponse addReplyComment(CommentReplyRequest commentReplyRequest){
        Member member = getMember();

        Long parentCommentId = commentReplyRequest.parentCommentId();
        String content = commentReplyRequest.content();

        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ProjectException(COMMENT_NOT_FOUND));

        Posting posting = parentComment.getPosting();

        Comment comment = Comment.builder()
                .posting(posting)
                .member(member)
                .content(content)
                .parentComment(parentComment)
                .build();

        parentComment.addReplies(comment);
        commentRepository.save(comment);

        PostingCommentResponse postingCommentResponse = PostingCommentResponse.fromEntity(comment);
        return postingCommentResponse;
    }

    public Comment getCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ProjectException(COMMENT_NOT_FOUND));
    }

    public Comment getDeleteCommentByCommentId(Long commentId) {
        return commentRepository.findDeletedCommentById(commentId)
                .orElseThrow(()-> new ProjectException(COMMENT_NOT_FOUND));
    }


    public List<PostingCommentResponse> getCommentResponse(Long postingId) {
        List<PostingCommentResponse> postingCommentResponseList = new ArrayList<>();
        List<Comment> commentList = commentRepository.findByPosting_IdOrderByDepth(postingId);
        Map<Long, PostingCommentResponse> postingCommentResponseMap = new HashMap<>();

        for (Comment comment : commentList) {
            if (comment.getDepth() == 1) {
                PostingCommentResponse postingCommentResponse = PostingCommentResponse.fromEntity(comment);
                postingCommentResponseList.add(postingCommentResponse);
                postingCommentResponseMap.put(comment.getId(), postingCommentResponse);
            } else { //depth >=2
                Long parentCommentId = comment.getParentComment().getId();
                PostingCommentResponse parentCommentResponse = postingCommentResponseMap.get(parentCommentId);
                PostingCommentResponse postingCommentResponse = PostingCommentResponse.fromEntity(comment);
                parentCommentResponse.addPostingCommentResponse(postingCommentResponse);
                postingCommentResponseMap.put(comment.getId(), postingCommentResponse);
            }
        }
        return postingCommentResponseList;
    }

    @Transactional
    public List<PostingCommentResponse> editComment(CommentEditRequest commentEditRequest){
        Long commentId = commentEditRequest.commentId();
        Comment comment = getCommentByCommentId(commentId);
        validateComment(comment);
        comment.updateContent(commentEditRequest.content());
        commentRepository.save(comment);

        Long postingId = comment.getPostingId();
        return getCommentResponse(postingId);
    }


    @Transactional
    public List<PostingCommentResponse> removeComment(CommentRemoveRequest commentRemoveRequest){
        Long commentId = commentRemoveRequest.commentId();
        Comment comment = getCommentByCommentId(commentId);
        validateComment(comment);
        comment.deleteComment();
        commentRepository.save(comment);

        Long postingId = comment.getPostingId();
        return getCommentResponse(postingId);
    }

    @Transactional
    public List<PostingCommentResponse> blockComment(CommentRemoveRequest commentRemoveRequest){
        Long commentId = commentRemoveRequest.commentId();
        Comment comment = getCommentByCommentId(commentId);
        comment.deleteComment();
        commentRepository.save(comment);

        Long postingId = comment.getPostingId();
        return getCommentResponse(postingId);
    }


    public boolean validateComment(Comment comment) {
        Long memberId = comment.getMember().getId();

        if (comment.getMember().getId() != memberId) {
            throw new ProjectException(COMMENT_REQUEST_MISMATCHING);
        }
        return true;
    }







    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }

}
