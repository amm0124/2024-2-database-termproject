package database.termproject.admin.controller;


import database.termproject.admin.dto.request.ManagerRegisterRequest;
import database.termproject.admin.dto.request.AdminMemberRequest;
import database.termproject.admin.service.AdminService;
import database.termproject.domain.member.dto.request.MemberSignUpRequestDto;
import database.termproject.domain.posting._comment.dto.request.CommentRemoveRequest;
import database.termproject.domain.posting.dto.request.PostingDeleteRequest;
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

    @PostMapping("/register/manager")
    public ResponseEntity<?> registerManager(@RequestBody ManagerRegisterRequest managerRegisterRequest){
        adminService.registerManager(managerRegisterRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto){
        adminService.registerAdmin(memberSignUpRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restore/member")
    public ResponseEntity<?> restoreMember(@RequestBody AdminMemberRequest memberRestoreRequest){
        adminService.restoreMemberByAdmin(memberRestoreRequest.memberId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/member")
    public ResponseEntity<?> deleteMember(@RequestBody AdminMemberRequest memberRestoreRequest){
        adminService.deleteMemberByAdmin(memberRestoreRequest.memberId());
        return ResponseEntity.ok().build();
    }
}
