package neostudy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dto.*;
import neostudy.exception.ApplicationError;
import neostudy.exception.NoElementException;
import neostudy.exception.SesCodeException;
import neostudy.model.ChangeType;
import neostudy.model.Status;
import neostudy.model.Theme;
import neostudy.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final ApplicationService applicationService;
    private final LoanOfferService loanOfferService;
    private final KafkaProducerService kafkaProducerService;
    private final SesCodeService sesCodeService;
    private final SummaryAppInfoService summaryAppInfoService;
    private final ApplicationDTOService applicationDTOService;

    @ApiOperation(value = "Запрос кредитных предложений")
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> getApplication(@ApiParam(value = "Предварительные данные для расчётов") @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("@PostMapping(/application): приняли LoanApplicationRequestDTO: {}", loanApplicationRequestDTO.toString());

        return new ResponseEntity<>(loanOfferService.creatingLoanOfferDTOList(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @ApiOperation(value = "Выбор кредитного предложения")
    @PutMapping("/offer")
    public ResponseEntity<?> choosingOffer(@ApiParam(value = "Выбраное подходящее предложение") @RequestBody LoanOfferDTO loanOfferDTO) throws NoElementException, JsonProcessingException {
        log.debug("@PutMapping(/offer): приняли LoanOfferDTO: {}", loanOfferDTO.toString());

        applicationService.choosingOffer(loanOfferDTO);
        kafkaProducerService.send("finish-registration", Theme.FINISH_REGISTRATION, loanOfferDTO.getApplicationId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Детализованный расчёт предложения")
    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<?> choosingApplication(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) throws NoElementException, JsonProcessingException, ApplicationError {
        log.debug("@PutMapping(/calculate/{}): приняли FinishRegistrationRequestDTO: {}", applicationId, finishRegistrationRequestDTO);

        applicationService.choosingApplication(applicationId, finishRegistrationRequestDTO);
        kafkaProducerService.send("create-documents", Theme.CREATE_DOCUMENTS, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Запрос на отправку документов")
    @PostMapping("/document/{applicationId}/send")
    public ResponseEntity<?> sendDocs(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId) throws JsonProcessingException, NoElementException, ApplicationError {

        applicationService.isStatusCorrect(applicationId, Status.CC_APPROVED);
        kafkaProducerService.send("send-documents", Theme.SEND_DOCUMENTS, applicationId);
        applicationService.setApplicationStatusHistory(applicationId, Status.DOCUMENT_CREATED, LocalDate.now(), ChangeType.AUTOMATIC);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Запрос на подписание документов")
    @PostMapping("/document/{applicationId}/sign")
    public ResponseEntity<?> singDocs(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId) throws JsonProcessingException, NoElementException, ApplicationError {

        applicationService.isStatusCorrect(applicationId, Status.DOCUMENT_CREATED);
        kafkaProducerService.send("send-ses", Theme.SEND_SES, applicationId);
        applicationService.setApplicationStatusHistory(applicationId, Status.PREPARE_DOCUMENTS, LocalDate.now(), ChangeType.AUTOMATIC);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Подписание документов")
    @PostMapping("/document/{applicationId}/code")
    public ResponseEntity<?> receiveSesCode(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId, @ApiParam(value = "Простая электронная подпись", example = "0000") @RequestBody Integer sesCode) throws JsonProcessingException, NoElementException, SesCodeException, ApplicationError {

        applicationService.isStatusCorrect(applicationId, Status.PREPARE_DOCUMENTS);
        sesCodeService.checkSesCode(
                applicationService.getApplication(applicationId).getSesCode(), sesCode, applicationId);
        applicationService.setSighDate(applicationId);
        kafkaProducerService.send("credit-issued", Theme.CREDIT_ISSUED, applicationId);
        applicationService.setApplicationStatusHistory(applicationId, Status.DOCUMENT_SIGNED, LocalDate.now(), ChangeType.AUTOMATIC);
        applicationService.setApplicationStatusHistory(applicationId, Status.CREDIT_ISSUED, LocalDate.now(), ChangeType.AUTOMATIC);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Технический эндпоинт (DTO для Dossier)")
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<SummaryAppInfo> getApplication(@PathVariable Long applicationId) throws NoElementException {
        return new ResponseEntity<>(summaryAppInfoService.getSummaryAppInfo(applicationId), HttpStatus.OK);
    }

    @GetMapping("/admin/application/{applicationId}")
    public ResponseEntity<ApplicationDTO> getApplicationDTO(@PathVariable Long applicationId) throws NoElementException {
        return new ResponseEntity<>(applicationDTOService.getAppDTO(applicationId), HttpStatus.OK);
    }

    @GetMapping("/admin/application/")
    public ResponseEntity<List<ApplicationDTO>> getAllApplicationDTOs() throws NoElementException {
        return new ResponseEntity<>(applicationDTOService.getAllAppDTO(), HttpStatus.OK);
    }
}
