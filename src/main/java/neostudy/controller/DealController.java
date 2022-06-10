package neostudy.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dto.*;
import neostudy.entity.*;
import neostudy.exception.NoElementException;
import neostudy.exception.ScoringException;
import neostudy.service.ApplicationService;
import neostudy.service.LoanOfferService;
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

    @ApiOperation(value = "Запрос кредитных предложений")
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> getApplication(@ApiParam(value = "Предварительные данные для расчётов") @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) throws ScoringException {
        log.debug("@PostMapping(/application): приняли LoanApplicationRequestDTO: {}", loanApplicationRequestDTO.toString());

        return new ResponseEntity<>(loanOfferService.creatingLoanOfferDTOList(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @ApiOperation(value = "Выбор кредитного предложения")
    @PutMapping("/offer")
    public ResponseEntity<?> choosingOffer(@ApiParam(value = "Выбраное подходящее предложение") @RequestBody LoanOfferDTO loanOfferDTO) throws NoElementException {
        log.debug("@PutMapping(/offer): приняли LoanOfferDTO: {}", loanOfferDTO.toString());

        applicationService.choosingOffer(loanOfferDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Детализованный расчёт предложения")
    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<?> choosingApplication(@ApiParam(value = "Номер кредитного предложения") @PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) throws NoElementException, ScoringException {
        log.debug("@PutMapping(/calculate/{}): приняли FinishRegistrationRequestDTO: {}", applicationId, finishRegistrationRequestDTO);

        applicationService.choosingApplication(applicationId, finishRegistrationRequestDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
