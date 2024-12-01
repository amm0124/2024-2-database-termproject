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

    //Posting
    POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 글입니다."),


    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 댓글입니다.")

    ;



    private final HttpStatus httpStatus;
    private final String message;

}
