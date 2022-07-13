package neostudy.service;

import neostudy.dto.FinishRegistrationRequestDTO;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.dto.LoanOfferDTO;
import neostudy.entity.Application;
import neostudy.exception.ApplicationError;
import neostudy.exception.NoElementException;
import neostudy.model.ChangeType;
import neostudy.model.Status;
//import neostudy.exception.ScoringException;

import java.time.LocalDate;
import java.util.List;

public interface ApplicationService {
    Application getApplication(Long id) throws NoElementException;

    List<Application> getAllApplications();

    Application initApplication(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void saveApplication(Application application);

    void choosingOffer(LoanOfferDTO loanOfferDTO) throws NoElementException;

    void choosingApplication(Long id, FinishRegistrationRequestDTO finishRegistrationRequestDTO) throws NoElementException, ApplicationError;

    void setSighDate(Long id) throws NoElementException;

    void setApplicationStatusHistory(Long applicationId, Status status, LocalDate date, ChangeType changeType) throws NoElementException;

    void isStatusCorrect(Long applicationId, Status status) throws NoElementException, ApplicationError;
}
