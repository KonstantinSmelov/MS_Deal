package neostudy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class ScoringException extends Exception {

    private String message;
}
