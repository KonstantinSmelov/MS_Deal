package neostudy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class NoElementException extends Exception {

    private String message;
}
