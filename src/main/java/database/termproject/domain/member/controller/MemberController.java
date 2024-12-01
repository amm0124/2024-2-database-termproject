package database.termproject.domain.member.controller;


import database.termproject.domain.member.dto.request.MemberSignUpRequestDto;
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
    @Secured("{ROLE_ADMIN, ROLE_MANAGER, ROLE_USER, ROLE_ANONYMOUS}")
    public String getMemberInfo() {
        return "HI";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberSignUpRequestDto memberSignUpRequestDto){
        memberService.signUp(memberSignUpRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email-verify")
    public ResponseEntity<?> verifyByEmail(){
        //TODO : 이메일 인증
        return ResponseEntity.ok().build();
    }




}

