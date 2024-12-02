package database.termproject.domain.posting._comment.controller;


import database.termproject.domain.posting._comment.dto.request.CommentReplyRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRequest;
import database.termproject.domain.posting._comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest) {
        commentService.addComment(commentRequest);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping("/reply")
    public ResponseEntity<?> createReply(@RequestBody CommentReplyRequest commentReplyRequest) {
        commentService.addReplyComment(commentReplyRequest);
        return ResponseEntity.ok().build();
    }


}
