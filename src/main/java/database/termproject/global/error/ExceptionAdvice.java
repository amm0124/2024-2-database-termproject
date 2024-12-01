package database.termproject.global.error;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ProjectException.class)
    public ErrorResponse businessExceptionHandler(ProjectException exception) {
        ProjectError error = exception.getProjectError();
        log.warn("{} : {}", error.name(), error.getMessage(), exception);
        return ErrorResponse.builder(exception, error.getHttpStatus(), error.getMessage())
                .title(error.name())
                .build();
    }



}
