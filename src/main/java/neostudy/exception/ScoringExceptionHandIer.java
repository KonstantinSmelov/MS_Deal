package neostudy.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import neostudy.model.Theme;
import neostudy.service.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@RequiredArgsConstructor
public class ScoringExceptionHandIer {

    private final KafkaProducerService kafkaProducerService;

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> wrongResponseFromFeign(FeignException e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    @ExceptionHandler(NoElementException.class)
    public ResponseEntity<String> NoElementFound(NoElementException e) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<String> ScoringDeny(ScoringException e){

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }

    @ExceptionHandler(SesCodeException.class)
    public ResponseEntity<String> WrongSesCode(SesCodeException e) throws JsonProcessingException, NoElementException {

        kafkaProducerService.send("application-denied", Theme.APPLICATION_DENIED, e.getId());

        return new ResponseEntity<>(e.getMessage(), HttpStatus.LOCKED);
    }
}
