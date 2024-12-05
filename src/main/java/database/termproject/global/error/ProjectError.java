package database.termproject.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ProjectError {

    // 회원
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    EXIST_MEMBER(HttpStatus.CONFLICT , "이미 존재하는 회원입니다."),
    ALREADY_VERIFY_MEMBER(HttpStatus.CONFLICT, "이미 인증한 회원입니다"),


    //Posting
    POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 글입니다."),
    POSTING_DELETE_REQUEST_MISMATCHING(HttpStatus.CONFLICT, "자신의 글이 아닙니다"),


    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 댓글입니다."),
    COMMENT_REQUEST_MISMATCHING(HttpStatus.CONFLICT, "자신의 댓글이 아닙니다"),

    //Matching
    MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 매칭입니다."),
    MATCHING_UPDATE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "현재 참여한 사람보다 바꾸려는 인원이 많습니다"),

    //Mail
    MAIL_EXCEPTION(HttpStatus.BAD_GATEWAY, "메일 처리 도중 예외 발생"),
    MAIL_CODE_GENERATING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "인증 코드 생성 중 오류 발생"),
    CODE_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "인증 코드가 잘못되었습니다"),
    CODE_EXPIRED_EXCEPTION(HttpStatus.BAD_REQUEST, "코드가 만료되었습니다"),

    //facilities
    FACILITIES_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 게임 시설이 없습니다"),

    //matching join
    MATCHING_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "참여한 매칭입니다."),
    MATCHING_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "매칭의 최대 인원을 초과하였습니다."),
    MATCHING_SINGLE_PLAYER_ONLY(HttpStatus.BAD_REQUEST, "게임 매칭은 한 명만 할 수 있습니다."),
    MATCHING_BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청 body 잘못 됨"),
    MATCHING_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "매칭 참여 정보를 찾을 수 없습니다"),
    MATCHING_JOIN_CANCELLED_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "나의 매칭은 취소 할 수 없습니다."),
    MATCHING_MISMATCHING(HttpStatus.BAD_REQUEST,"나의 매칭이 아닙니다."),

    //LIKE
    LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 좋아요를 눌렀습니다."),
    DISLIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 싫어요를 눌렀습니다.")



    ;



    private final HttpStatus httpStatus;
    private final String message;

}
