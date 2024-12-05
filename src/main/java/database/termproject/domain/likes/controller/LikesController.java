package database.termproject.domain.likes.controller;


import database.termproject.domain.likes.dto.request.LikeRequest;
import database.termproject.domain.likes.entity.LikesType;
import database.termproject.domain.likes.service.LikesService;
import database.termproject.domain.posting.service.PostingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final PostingServiceImpl postingService;

    @PostMapping("/add")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_ANONYMOUS"})
    public ResponseEntity<?> addLike(@RequestBody LikeRequest likeRequest){
        likesService.addLikes(likeRequest, LikesType.LIKES);
        return ResponseEntity.ok(postingService.
                getPostingDetailResponse(likeRequest.postingId())
        );
    }

    @PostMapping("/subtract")
    public ResponseEntity<?> subtractLike(@RequestBody LikeRequest likeRequest){
        likesService.addLikes(likeRequest, LikesType.DISLIKES);
        return ResponseEntity.ok().build();
    }


}
