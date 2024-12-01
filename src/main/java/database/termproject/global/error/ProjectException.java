package database.termproject.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProjectException extends RuntimeException {
    private final ProjectError projectError;
}