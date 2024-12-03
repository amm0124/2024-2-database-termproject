package database.termproject.domain.member.controller;


import database.termproject.domain.member.dto.request.MemberEditRequest;
import database.termproject.domain.member.dto.request.MemberEmailVerifyRequest;
import database.termproject.domain.member.dto.request.MemberSignUpRequestDto;
import database.termproject.domain.member.dto.response.MemberResponse;
import database.termproject.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> getMemberInfo() {
        return ResponseEntity.ok(memberService.
                getMemberInfo());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto){
        memberService.signUp(memberSignUpRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/try-verify")
    @Secured({"ROLE_ANONYMOUS"})
    public ResponseEntity<?> tryVerify(){
        memberService.generateAuthCode();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email-code-verify")
    @Secured({"ROLE_ANONYMOUS"})
    public ResponseEntity<?> verifyByEmail(@RequestBody MemberEmailVerifyRequest memberEmailVerifyRequest){

        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> editMember(@RequestBody MemberEditRequest memberEditRequest){
        return ResponseEntity.ok(
                memberService.editMemberInfo(memberEditRequest)
        );
    }

    @DeleteMapping("/remove")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> removeMember(){
        return ResponseEntity.ok(
                memberService.softDeleteMember()
        );
    }

    //TODO : 복구 로직 작성해야 함
    @PostMapping("/restore")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> restoreMember(){
        return ResponseEntity.ok().build();
    }

}

