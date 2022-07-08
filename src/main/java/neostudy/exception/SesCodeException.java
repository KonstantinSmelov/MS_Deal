package neostudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SesCodeException extends Exception {
    private String message;
    private Long id;
}
