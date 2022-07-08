package neostudy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoElementException extends Exception {
    private String message;
}
