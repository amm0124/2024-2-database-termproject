package database.termproject.domain.posting._comment.controller;


import database.termproject.domain.posting._comment.dto.request.CommentEditRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRemoveRequest;
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
        return ResponseEntity.ok(
                commentService.addComment(commentRequest)
        );
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping("/reply")
    public ResponseEntity<?> createReply(@RequestBody CommentReplyRequest commentReplyRequest) {
        return ResponseEntity.ok(
                commentService.addReplyComment(commentReplyRequest)
        );
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PutMapping("/edit")
    public ResponseEntity<?> updateComment(@RequestBody CommentEditRequest commentEditRequest) {
        return ResponseEntity.ok(
                commentService.editComment(commentEditRequest)
        );
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeComment(@RequestBody CommentRemoveRequest commentRemoveRequest) {
        return ResponseEntity.ok(
                commentService.removeComment(commentRemoveRequest)
        );
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/block")
    public ResponseEntity<?> blockComment(@RequestBody CommentRemoveRequest commentRemoveRequest) {
        return ResponseEntity.ok(
                commentService.blockComment(commentRemoveRequest)
        );
    }

}
