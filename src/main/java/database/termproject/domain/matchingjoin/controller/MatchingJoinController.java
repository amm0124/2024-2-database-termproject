package database.termproject.domain.matchingjoin.controller;

import database.termproject.domain.matchingjoin.service.MatchingJoinService;
import database.termproject.domain.posting.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchingJoinController {

    private final MatchingJoinService matchingJoinService;

}
