package neostudy.controller;

import lombok.extern.slf4j.Slf4j;
import neostudy.*;
import neostudy.dto.*;
import neostudy.entity.*;
import neostudy.feignclient.ConveyorClient;
import neostudy.service.ApplicationService;
import neostudy.service.ClientService;
import neostudy.service.CreditService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
public class DealController {

    private final ConveyorClient conveyorClient;
    private final ModelMapper modelMapper;
    private final ClientService clientService;
    private final CreditService creditService;
    private final ApplicationService applicationService;

    public DealController(ConveyorClient conveyorClient, ModelMapper modelMapper, ClientService clientService, CreditService creditService, ApplicationService applicationService) {
        this.conveyorClient = conveyorClient;
        this.modelMapper = modelMapper;
        this.clientService = clientService;
        this.creditService = creditService;
        this.applicationService = applicationService;
    }

    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> getApplication(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("@PostMapping(/application): приняли LoanApplicationRequestDTO: {}", loanApplicationRequestDTO.toString());

        Employment employment = new Employment();
        Passport passport = modelMapper.map(loanApplicationRequestDTO, Passport.class);
        Client client = modelMapper.map(loanApplicationRequestDTO, Client.class);
        client.setEmployment(employment);
        client.setPassport(passport);
        clientService.saveClient(client);
        log.info("@PostMapping(/application): в БД создан Client: {}", client);

        List<PaymentSchedule> paymentScheduleList = new ArrayList<>();
        Credit credit = Credit.builder().paymentScheduleList(paymentScheduleList).build();
        creditService.saveCredit(credit);
        log.info("@PostMapping(/application): в БД создан Credit: {}", credit);

        AppliedOffer appliedOffer = new AppliedOffer();
        ApplicationStatusHistory applicationStatusHistory = ApplicationStatusHistory.builder()
                .status(Status.PREAPPROVAL)
                .time(LocalDate.now())
                .changeType(ChangeType.AUTOMATIC).build();
        List<ApplicationStatusHistory> applicationStatusHistoryList = new ArrayList<>();
        Application application = Application.builder()
                .client(client)
                .credit(credit)
                .appliedOffer(appliedOffer)
                .applicationStatusHistoriesList(applicationStatusHistoryList)
                .creationDate(LocalDate.now())
                .status(Status.PREAPPROVAL)
                .build();
        application.addStatusHistoryToApp(applicationStatusHistory);
        applicationService.saveApplication(application);
        log.info("@PostMapping(/application): в БД создан Application: {}", application);

        List<LoanOfferDTO> loanOfferDTOList = conveyorClient.getLoanOfferListFromConveyor(loanApplicationRequestDTO);
        log.info("@PostMapping(/application): loanOfferDTOList из Conveyor: {}", loanOfferDTOList);

        loanOfferDTOList.forEach(s -> s.setApplicationId(application.getId()));
        log.info("@PostMapping(/application): return loanOfferDTOList: {}", loanOfferDTOList);

        return new ResponseEntity<>(loanOfferDTOList, HttpStatus.OK);
    }

    @PutMapping("/offer")
    public ResponseEntity<?> choosingOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("@PutMapping(/offer): приняли LoanOfferDTO: {}", loanOfferDTO.toString());

        Application application = applicationService.getApplication(loanOfferDTO.getApplicationId());
        log.info("@PutMapping(/offer): достали из БД application: {}", application);

        ApplicationStatusHistory applicationStatusHistory = ApplicationStatusHistory.builder()
                .status(Status.APPROVED)
                .time(LocalDate.now())
                .changeType(ChangeType.MANUAL).build();
        application.addStatusHistoryToApp(applicationStatusHistory);
        log.info("@PutMapping(/offer): добавили в Application appStatusHistory: {}", applicationStatusHistory);

        AppliedOffer appliedOffer = modelMapper.map(loanOfferDTO, AppliedOffer.class);
        application.setAppliedOffer(appliedOffer);
        application.setStatus(Status.CC_APPROVED);
        log.info("@PutMapping(/offer): добавили в Application appliedOffer: {}", appliedOffer);

        applicationService.saveApplication(application);
        log.info("@PutMapping(/offer): сохранили в БД Application: {}", application);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<?> choosingApplication(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("@PutMapping(/calculate/{}): приняли FinishRegistrationRequestDTO: {}", applicationId, finishRegistrationRequestDTO);

        Application application = applicationService.getApplication(applicationId);
        log.info("@PutMapping(/calculate/{}): взяли Application из БД: {}", applicationId, application);

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()  //TODO попробовать реализовать через modelMapper
                .amount(application.getAppliedOffer().getRequestedAmount())
                .term(application.getAppliedOffer().getTerm())
                .firstName(application.getClient().getFirstName())
                .lastName(application.getClient().getLastName())
                .middleName(application.getClient().getMiddleName())
                .gender(finishRegistrationRequestDTO.getGender())
                .birthdate(application.getClient().getBirthdate())
                .passportSeries(application.getClient().getPassport().getPassportSeries())
                .passportNumber(application.getClient().getPassport().getPassportNumber())
                .passportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .passportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .dependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .employment(finishRegistrationRequestDTO.getEmployment())
                .account(finishRegistrationRequestDTO.getAccount())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient()).build();
        log.info("@PutMapping(/calculate/{}): собрали ScoringDataDTO: {}", applicationId, scoringDataDTO);

        Client client = clientService.getClient(application.getId());
        modelMapper.map(finishRegistrationRequestDTO, client);
        log.info("@PutMapping(/calculate/{}): дополнили Client из FinishRegistrationRequestDTO: {}", applicationId, client);

        CreditDTO creditDTO = conveyorClient.getScoringDataFromConveyor(scoringDataDTO);
        log.info("@PutMapping(/calculate/{}): собрали CreditDTO из Conveyor на основе собранной ScoringDataDTO: {}", applicationId, creditDTO);

        Credit credit = creditService.getCredit(application.getId());
        credit.setAmount(creditDTO.getAmount());
        credit.setTerm(creditDTO.getTerm());
        credit.setMonthlyPayment(creditDTO.getMonthlyPayment());
        credit.setRate(creditDTO.getRate());
        credit.setPsk(creditDTO.getPsk());
        credit.setIsInsuranceEnabled(creditDTO.getIsInsuranceEnabled());
        credit.setIsSalaryClient(creditDTO.getIsSalaryClient());

        log.info("@PutMapping(/calculate/{}): создали Credit из CreditDTO: {}", applicationId, credit);

        for (int x = 0; x < creditDTO.getPaymentSchedule().size(); x++) {
            PaymentSchedule paymentSchedule = modelMapper.map(creditDTO.getPaymentSchedule().get(x), PaymentSchedule.class);
            credit.addScheduleElementToCredit(paymentSchedule);
        }

        credit.setCreditStatus(CreditStatus.CALCULATED);
        creditService.saveCredit(credit);
        log.info("@PutMapping(/calculate/{}): дополнили Credit данными и сохранили в БД: {}", applicationId, credit);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
