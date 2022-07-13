package neostudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dao.ApplicationRepository;
import neostudy.dto.*;
import neostudy.entity.*;
import neostudy.exception.ApplicationError;
import neostudy.exception.NoElementException;
import neostudy.feignclient.ConveyorClient;
import neostudy.model.ChangeType;
import neostudy.model.CreditStatus;
import neostudy.model.StaticID;
import neostudy.model.Status;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ClientService clientService;
    private final CreditService creditService;
    private final ModelMapper modelMapper;
    private final ConveyorClient conveyorClient;
    private final SesCodeService sesCodeService;

    @Override
    public Application getApplication(Long id) throws NoElementException {
        return applicationRepository.findById(id).orElseThrow(() -> new NoElementException("\nApplication с таким номером в БД нет"));
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Application initApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = clientService.initClient(loanApplicationRequestDTO);
        Credit credit = creditService.initCredit();
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
                .applicationStatusHistoryList(applicationStatusHistoryList)
                .creationDate(LocalDate.now())
                .status(Status.PREAPPROVAL)
                .build();

        application.addStatusHistoryToApp(applicationStatusHistory);

        applicationRepository.save(application);
        log.debug("initApplication(): в БД инициализирован Application: {}", application);

        return application;
    }

    @Override
    public void saveApplication(Application application) {
        applicationRepository.save(application);
        log.debug("saveApplication(): в БД сохранён Application: {}", application);
    }

    @Override
    public void choosingOffer(LoanOfferDTO loanOfferDTO) throws NoElementException {
//        Application application;
//        try {
//            application = getApplication(loanOfferDTO.getApplicationId());
//        } catch (NoElementException e) {
//            throw new NoElementException("\nApplication с таким номером в БД нет");
//        }
        Application application = getApplication(loanOfferDTO.getApplicationId());
        log.debug("choosingOffer(): достали из БД application: {}", application);

        AppliedOffer appliedOffer = modelMapper.map(loanOfferDTO, AppliedOffer.class);
        application.setAppliedOffer(appliedOffer);
        log.debug("choosingOffer(): добавили в Application appliedOffer: {}", appliedOffer);

        setApplicationStatusHistory(application.getId(), Status.APPROVED, LocalDate.now(), ChangeType.MANUAL);

        applicationRepository.save(application);
        log.debug("choosingOffer(): сохранили в БД Application: {}", application);
    }

    @Override
    public void choosingApplication(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) throws NoElementException, ApplicationError {
//        Application application;
//        try {
//            application = getApplication(applicationId);
//        } catch (NoElementException e) {
//            throw new NoElementException("\nApplication с таким номером в БД нет");
//        }
        Application application = getApplication(applicationId);
        log.debug("choosingApplication(): взяли Application №{} из БД: {}", applicationId, application);


        isStatusCorrect(applicationId, Status.APPROVED);

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
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
        log.debug("choosingApplication(): собрали ScoringDataDTO: {}", scoringDataDTO);

        Client client = clientService.getClient(application.getId());
        modelMapper.map(finishRegistrationRequestDTO, client);
        log.debug("choosingApplication(): дополнили Client из FinishRegistrationRequestDTO: {}", client);

        StaticID.applicationId = applicationId;
        CreditDTO creditDTO = conveyorClient.getScoringDataFromConveyor(scoringDataDTO);

        log.debug("choosingApplication(): собрали CreditDTO из Conveyor на основе собранной ScoringDataDTO: {}", creditDTO);

        Credit credit = creditService.getCredit(application.getId());
        credit.setAmount(creditDTO.getAmount());
        credit.setTerm(creditDTO.getTerm());
        credit.setMonthlyPayment(creditDTO.getMonthlyPayment());
        credit.setRate(creditDTO.getRate());
        credit.setPsk(creditDTO.getPsk());
        credit.setIsInsuranceEnabled(creditDTO.getIsInsuranceEnabled());
        credit.setIsSalaryClient(creditDTO.getIsSalaryClient());

        log.debug("choosingApplication(): создали Credit из CreditDTO: {}", credit);

        for (int x = 0; x < creditDTO.getPaymentSchedule().size(); x++) {
            PaymentSchedule paymentSchedule = modelMapper.map(creditDTO.getPaymentSchedule().get(x), PaymentSchedule.class);
            credit.addScheduleElementToCredit(paymentSchedule);
        }

        credit.setCreditStatus(CreditStatus.CALCULATED);
        creditService.saveCredit(credit);
        log.debug("choosingApplication(): дополнили Credit данными и сохранили в БД: {}", credit);

        setSesCode(applicationId);
        setApplicationStatusHistory(applicationId, Status.CC_APPROVED, LocalDate.now(), ChangeType.AUTOMATIC);
    }

    public void setSighDate(Long applicationId) throws NoElementException {
        Application application = getApplication(applicationId);

        application.setSignDate(LocalDate.now());
        log.debug("setSighDate(): установили signDate {}", application.getSignDate());

        applicationRepository.save(application);
        log.debug("setSighDate(): дополнили Application данными и сохранили в БД: {}", application);
    }

    private void setSesCode(Long applicationId) throws NoElementException {
        Application application = getApplication(applicationId);

        application.setSesCode(sesCodeService.generateSesCode());
        log.debug("setSesCode(): установили sesCode {}", application.getSesCode());

        applicationRepository.save(application);
        log.debug("setSesCode(): дополнили Application данными и сохранили в БД: {}", application);
    }

    public void setApplicationStatusHistory(Long applicationId, Status status, LocalDate time, ChangeType changeType) throws NoElementException {
        Application application = getApplication(applicationId);
        application.setStatus(status);

        ApplicationStatusHistory applicationStatusHistory = ApplicationStatusHistory.builder()
                .status(status)
                .time(time)
                .changeType(changeType).build();
        application.addStatusHistoryToApp(applicationStatusHistory);
        log.debug("setApplicationStatusHistory(): добавили в Application appStatusHistory: {}", applicationStatusHistory);

        log.debug("setApplicationStatusHistory(): для Application {} установили Status {}", applicationId, status);

        applicationRepository.save(application);
        log.debug("setApplicationStatusHistory(): сохранили в БД: {}", application);
    }

    public void isStatusCorrect(Long applicationId, Status correctStatus) throws NoElementException, ApplicationError {
        Application application = getApplication(applicationId);

        if (application.getStatus() != correctStatus) {
            throw new ApplicationError(String.format("\nПредложение c №%d не подходит для данного этапа", applicationId));
        }

    }
}
