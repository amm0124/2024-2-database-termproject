package database.termproject.domain.util.controller;


import database.termproject.domain.member.dto.request.MemberSignUpRequestDto;
import database.termproject.domain.member.entity.Role;
import database.termproject.domain.util.service.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/util")
public class UtilController {

    private final UtilService utilService;

    @PostMapping("/admin")
    public ResponseEntity<?> changeAdmin(){
        utilService.change(Role.ROLE_ADMIN);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/manager")
    public ResponseEntity<?> changeManager(){
        utilService.change(Role.ROLE_MANAGER);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user")
    public ResponseEntity<?> changeUser(){
        utilService.change(Role.ROLE_USER);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/anonymous")
    public ResponseEntity<?> changeAnonymous(){
        utilService.change(Role.ROLE_ANONYMOUS);
        return ResponseEntity.ok().build();
    }

}
