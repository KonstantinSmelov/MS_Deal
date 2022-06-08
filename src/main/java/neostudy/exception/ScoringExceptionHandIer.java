package neostudy.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ScoringExceptionHandIer {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> wrongResponseFromFeign(FeignException e) {

        String errorsList = e.getMessage();

        return new ResponseEntity<>(errorsList, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

}
