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

        String error = e.getMessage();

        return new ResponseEntity<>(error, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    @ExceptionHandler(NoElementException.class)
    public ResponseEntity<String> NoElementFound(NoElementException e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<String> ScoringDeny(ScoringException e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }


}
