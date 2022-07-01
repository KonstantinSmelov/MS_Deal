package neostudy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dto.FinishRegistrationRequestDTO;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.dto.LoanOfferDTO;
import neostudy.dto.SummaryAppInfo;
import neostudy.exception.NoElementException;
import neostudy.exception.ScoringException;
import neostudy.exception.SesCodeException;
import neostudy.model.Theme;
import neostudy.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "Запрос кредитных предложений")
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> getApplication(@ApiParam(value = "Предварительные данные для расчётов") @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) throws ScoringException {
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
    public ResponseEntity<?> choosingApplication(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) throws NoElementException, ScoringException, JsonProcessingException {
        log.debug("@PutMapping(/calculate/{}): приняли FinishRegistrationRequestDTO: {}", applicationId, finishRegistrationRequestDTO);

        applicationService.choosingApplication(applicationId, finishRegistrationRequestDTO);

        kafkaProducerService.send("create-documents", Theme.CREATE_DOCUMENTS, applicationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Запрос на отправку документов")
    @PostMapping("/document/{applicationId}/send")
    public ResponseEntity<?> sendDocs(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId) throws JsonProcessingException, NoElementException {

        kafkaProducerService.send("send-documents", Theme.SEND_DOCUMENTS, applicationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Запрос на подписание документов")
    @PostMapping("/document/{applicationId}/sign")
    public ResponseEntity<?> singDocs(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId) throws JsonProcessingException, NoElementException {

        kafkaProducerService.send("send-ses", Theme.SEND_SES, applicationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Подписание документов")
    @PostMapping("/document/{applicationId}/code")
    public ResponseEntity<?> receiveSesCode(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId, @ApiParam(value = "Простая электронная подпись", example = "0000") @RequestBody Integer sesCode) throws JsonProcessingException, NoElementException, SesCodeException {

        sesCodeService.checkSesCode(
                applicationService.getApplication(applicationId).getSesCode(), sesCode, applicationId);

        applicationService.setSighDate(applicationId);

        kafkaProducerService.send("credit-issued", Theme.CREDIT_ISSUED, applicationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Технический эндпоинт (данные для Dossier)")
    @PostMapping("/application/{applicationId}")
    public ResponseEntity<SummaryAppInfo> getApplication(@PathVariable Long applicationId) throws NoElementException {

        SummaryAppInfo summaryAppInfo = summaryAppInfoService.getSummaryAppInfo(applicationId);

        return new ResponseEntity<>(summaryAppInfo, HttpStatus.OK);
    }


}
