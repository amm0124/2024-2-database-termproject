package database.termproject.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ProjectError {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    EXIST_MEMBER(HttpStatus.CONFLICT , "이미 존재하는 회원입니다.")

    ;



    private final HttpStatus httpStatus;
    private final String message;

}
