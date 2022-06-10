package neostudy.controller;

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

    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> getApplication(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) throws ScoringException {
        log.debug("@PostMapping(/application): приняли LoanApplicationRequestDTO: {}", loanApplicationRequestDTO.toString());

        return new ResponseEntity<>(loanOfferService.creatingLoanOfferDTOList(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/offer")
    public ResponseEntity<?> choosingOffer(@RequestBody LoanOfferDTO loanOfferDTO) throws NoElementException {
        log.debug("@PutMapping(/offer): приняли LoanOfferDTO: {}", loanOfferDTO.toString());

        applicationService.choosingOffer(loanOfferDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<?> choosingApplication(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) throws NoElementException, ScoringException {
        log.debug("@PutMapping(/calculate/{}): приняли FinishRegistrationRequestDTO: {}", applicationId, finishRegistrationRequestDTO);

        applicationService.choosingApplication(applicationId, finishRegistrationRequestDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
