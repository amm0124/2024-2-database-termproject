package database.termproject.domain.posting._comment.controller;


import database.termproject.domain.posting._comment.dto.request.CommentReplyRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRequest;
import database.termproject.domain.posting._comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/base-comment")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest) {
        commentService.createComment(commentRequest);
        return ResponseEntity.ok().build();
    }

   /* @PostMapping("/reply-comment")
    public ResponseEntity<?> createReply(@RequestBody CommentReplyRequest commentReplyRequest) {
        commentService.commentReply(commentReplyRequest);
        return ResponseEntity.ok().build();
    }*/

}
