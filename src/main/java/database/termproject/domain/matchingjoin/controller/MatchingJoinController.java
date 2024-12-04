package database.termproject.domain.matchingjoin.controller;

import database.termproject.domain.matchingjoin.dto.request.MatchingJoinRequest;
import database.termproject.domain.matchingjoin.dto.response.MatchingJoinResponse;
import database.termproject.domain.matchingjoin.service.MatchingJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/join")
@RequiredArgsConstructor
public class MatchingJoinController {

    private final MatchingJoinService matchingJoinService;

    @PostMapping()
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> createMatchingJoin(@RequestBody MatchingJoinRequest matchingJoinRequest){
        List<MatchingJoinResponse> matchingJoinResponseList =  matchingJoinService.matchingJoin(matchingJoinRequest);
        return ResponseEntity.ok(matchingJoinResponseList);
    }

}
