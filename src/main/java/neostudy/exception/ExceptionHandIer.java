package neostudy.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import neostudy.model.ChangeType;
import neostudy.model.StaticID;
import neostudy.model.Status;
import neostudy.model.Theme;
import neostudy.service.ApplicationService;
import neostudy.service.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;


@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandIer {

    private final KafkaProducerService kafkaProducerService;
    private final ApplicationService applicationService;

    @ExceptionHandler(NoElementException.class)
    public ResponseEntity<String> NoElementFound(NoElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApplicationError.class)
    public ResponseEntity<String> applicationIdIsBusy(ApplicationError e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.LOCKED);
    }

    @ExceptionHandler(SesCodeException.class)
    public ResponseEntity<String> WrongSesCode(SesCodeException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.LOCKED);
    }

    @ExceptionHandler(FeignException.FeignClientException.class)
    public ResponseEntity<String> scoringInvalidFromConveyor(FeignException.FeignClientException e) throws JsonProcessingException, NoElementException {

        if (e.status() == HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value()) {
            applicationService.setApplicationStatusHistory(StaticID.applicationId, Status.CC_DENIED, LocalDate.now(), ChangeType.AUTOMATIC);
            kafkaProducerService.send("application-denied", Theme.APPLICATION_DENIED, StaticID.applicationId);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
        }
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
