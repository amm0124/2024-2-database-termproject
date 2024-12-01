package database.termproject.domain.posting.controller;

import database.termproject.domain.posting.dto.request.PostingRequest;
import database.termproject.domain.posting.service.PostingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/posting")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostingController {

    private final PostingServiceImpl postingService;

    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    @PostMapping("/free")
    public ResponseEntity<?> createFreePosting(PostingRequest postingRequest) {
        postingService.createFreePosting(postingRequest);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/tip")
    public ResponseEntity<?> createTipPosting(PostingRequest postingRequest) {
        //TODO : 세션에서 member 객체로 변환해야 함
        postingService.createFreePosting(postingRequest);
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/matching")
    public ResponseEntity<?> createMatchingPosting(PostingRequest postingRequest) {
        //TODO : 세션에서 member 객체로 변환해야 함
        postingService.createFreePosting(postingRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllPostings() {
        return ResponseEntity.ok().build();
    }

}
