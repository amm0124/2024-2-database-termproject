package database.termproject.admin.controller;


import database.termproject.admin.service.AdminService;
import database.termproject.domain.posting._comment.dto.request.CommentRemoveRequest;
import database.termproject.domain.posting._comment.dto.request.CommentRequest;
import database.termproject.domain.posting.controller.PostingController;
import database.termproject.domain.posting.dto.request.PostingDeleteRequest;
import database.termproject.domain.posting.dto.request.PostingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final AdminService adminService;

    @DeleteMapping("/delete/posting")
    public ResponseEntity<?> deletePosting(@RequestBody PostingDeleteRequest postingDeleteRequest){
        adminService.deletePostingByAdmin(postingDeleteRequest.postingId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/restore/posting")
    public ResponseEntity<?> restorePosting(@RequestBody PostingDeleteRequest postingDeleteRequest){
        adminService.restorePostingByAdmin(postingDeleteRequest.postingId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentRemoveRequest commentRemoveRequest){
        adminService.deleteCommentByAdmin(commentRemoveRequest.commentId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/restore/comment")
    public ResponseEntity<?> restoreComment(@RequestBody CommentRemoveRequest commentRemoveRequest){
        adminService.restoreCommentByAdmin(commentRemoveRequest.commentId());
        return ResponseEntity.ok().build();
    }

}
