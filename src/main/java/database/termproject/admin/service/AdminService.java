package database.termproject.admin.service;


import database.termproject.domain.posting._comment.entity.Comment;
import database.termproject.domain.posting._comment.service.CommentService;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.service.PostingServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PostingServiceImpl postingService;
    private final CommentService commentService;

    @Transactional
    public void deletePostingByAdmin(Long postingId){
        Posting posting = postingService.getPostingByPostingId(postingId);
        posting.softDelete();
    }

    @Transactional
    public void restorePostingByAdmin(Long postingId){
        Posting posting = postingService.getDeletePostingByPostingId(postingId);
        posting.restore();
    }

    @Transactional
    public void deleteCommentByAdmin(Long commentId){
        Comment comment = commentService.getCommentByCommentId(commentId);
        comment.softDelete();
    }


    @Transactional
    public void restoreCommentByAdmin(Long commentId){
        Comment comment = commentService.getDeleteCommentByCommentId(commentId);
        comment.restoreComment();
    }



}
