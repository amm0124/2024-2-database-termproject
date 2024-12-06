package database.termproject.domain.posting.controller;

import database.termproject.domain.posting.dto.request.*;
import database.termproject.domain.posting.dto.response.PostingDetailResponse;
import database.termproject.domain.posting.dto.response.PostingResponse;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.entity.PostingType;
import database.termproject.domain.posting.service.PostingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/posting")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostingController {

    private final PostingServiceImpl postingService;
        
    //TODO : 나중에 anonymous 없어야 함
    //@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER"})
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping("/free")
    public ResponseEntity<?> createFreePosting(@RequestBody PostingRequest postingRequest) {
        Posting posting = postingService.createPosting(postingRequest, PostingType.FREE);
        return ResponseEntity.ok(
                PostingDetailResponse.from(PostingResponse.fromEntity(posting),
                        null,
                        null)
        );
    }

    //@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER"})
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping("/tip")
    public ResponseEntity<?> createTipPosting(@RequestBody PostingRequest postingRequest) {
        Posting posting = postingService.createPosting(postingRequest, PostingType.TIP);
        return ResponseEntity.ok(
                PostingDetailResponse.from(PostingResponse.fromEntity(posting),
                        null,
                        null)
        );
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping("/notice")
    public ResponseEntity<?> createNoticePosting(@RequestBody PostingRequest postingRequest) {
        Posting posting = postingService.createPosting(postingRequest, PostingType.NOTICE);
        return ResponseEntity.ok(
                PostingDetailResponse.from(PostingResponse.fromEntity(posting),
                        null,
                        null)
        );
    }


    //@Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER"})
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping("/matching")
    public ResponseEntity<?> createMatchingPosting(@RequestBody MatchingTournamentPostingRequest matchingTournamentPostingRequest) {
        return ResponseEntity.ok(
                postingService.createMatchingPosting(matchingTournamentPostingRequest, PostingType.MATCHING));
    }

    //@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    @PostMapping("/tournament")
    public ResponseEntity<?> createTournamentPosting(@RequestBody MatchingTournamentPostingRequest matchingTournamentPostingRequest) {
        return ResponseEntity.ok(
                postingService.createMatchingPosting(matchingTournamentPostingRequest, PostingType.TOURNAMENT));
    }


    //GET
    @GetMapping("/free")
    public ResponseEntity<?> getFreePosting() {
        return ResponseEntity.ok(
                postingService.getPosting(PostingType.FREE)
        );
    }

    @GetMapping("/tip")
    public ResponseEntity<?> getTipPosting() {
        return ResponseEntity.ok(
                postingService.getPosting(PostingType.TIP)
        );
    }

    @GetMapping("/notice")
    public ResponseEntity<?> getNoticePosting() {
        return ResponseEntity.ok(
                postingService.getPosting(PostingType.NOTICE)
        );
    }


    @GetMapping("/matching")
    public ResponseEntity<?> getMatchingPosting() {
        return ResponseEntity.ok(
                postingService.getPosting(PostingType.MATCHING)
        );
    }

    @GetMapping("/tournament")
    public ResponseEntity<?> getTournamentPosting() {
        return ResponseEntity.ok(
                postingService.getPosting(PostingType.TOURNAMENT)
        );
    }



    //DELETE
    @DeleteMapping
    public ResponseEntity<?> deletePosting(@RequestBody PostingDeleteRequest postingDeleteRequest) {
        postingService.deletePosting(postingDeleteRequest);
        return ResponseEntity.noContent().build();
    }

    //PUT
    @PutMapping
    public ResponseEntity<?> updatePosting(@RequestBody UpdatePostingRequest updatePostingRequest) {
        return ResponseEntity.ok(
                postingService.updatePosting(updatePostingRequest)
        );
    }

    @PutMapping("/matching")
    public ResponseEntity<?> updateMatching(@RequestBody MatchingEditRequest matchingEditRequest){
        return ResponseEntity.ok(postingService.updateMatching(matchingEditRequest));
    }



}
