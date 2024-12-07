package database.termproject.domain.matchingjoin.controller;

import database.termproject.domain.matchingjoin.dto.request.EditMatchingJoinRequest;
import database.termproject.domain.matchingjoin.dto.request.MatchingJoinRequest;
import database.termproject.domain.matchingjoin.dto.response.MatchingJoinResponse;
import database.termproject.domain.matchingjoin.service.MatchingJoinService;
import database.termproject.domain.posting.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/join")
@RequiredArgsConstructor
public class MatchingJoinController {

    private final MatchingJoinService matchingJoinService;
    private final MatchingService matchingService;

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> createMatchingJoin(@RequestBody MatchingJoinRequest matchingJoinRequest){
        List<MatchingJoinResponse> matchingJoinResponseList = matchingJoinService.createMatchingJoin(matchingJoinRequest);
        return ResponseEntity.ok(matchingJoinResponseList);
    }

    @PutMapping("/cancel")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> cancelMatchingJoin(@RequestBody EditMatchingJoinRequest editMatchingJoinRequest){
        Long matchingId = matchingJoinService.cancel(editMatchingJoinRequest);
        List<MatchingJoinResponse> matchingJoinResponseList = matchingJoinService.getMatchingJoins(matchingId);
        return ResponseEntity.ok(matchingJoinResponseList);
    }

    @PutMapping("/tournament/edit")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> editMatchingJoin(@RequestBody EditMatchingJoinRequest editMatchingJoinRequest){
        Long matchingId = matchingJoinService.edit(editMatchingJoinRequest);
        List<MatchingJoinResponse> matchingJoinResponseList = matchingJoinService.getMatchingJoins(matchingId);
        return ResponseEntity.ok(matchingJoinResponseList);
    }

}
