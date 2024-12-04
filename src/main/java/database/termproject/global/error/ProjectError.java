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
    
    
    //Mail
    MAIL_EXCEPTION(HttpStatus.BAD_GATEWAY, "메일 처리 도중 예외 발생"),
    MAIL_CODE_GENERATING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "인증 코드 생성 중 오류 발생"),
    CODE_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "인증 코드가 잘못되었습니다"),
    CODE_EXPIRED_EXCEPTION(HttpStatus.BAD_REQUEST, "코드가 만료되었습니다")
    ;



    private final HttpStatus httpStatus;
    private final String message;

}
