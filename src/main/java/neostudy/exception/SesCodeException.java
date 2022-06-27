package neostudy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class SesCodeException extends Exception {

    private String message;

    @Setter
    @Getter
    private Long id;
}
