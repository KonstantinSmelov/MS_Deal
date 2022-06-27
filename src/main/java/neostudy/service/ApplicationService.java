package neostudy.service;

import neostudy.dto.FinishRegistrationRequestDTO;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.dto.LoanOfferDTO;
import neostudy.entity.Application;
import neostudy.exception.NoElementException;
import neostudy.exception.ScoringException;

public interface ApplicationService {
    Application getApplication(Long id) throws NoElementException;
    Application initApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) throws ScoringException;
    void saveApplication(Application application);
    void choosingOffer(LoanOfferDTO loanOfferDTO) throws NoElementException;
    void choosingApplication(Long id, FinishRegistrationRequestDTO finishRegistrationRequestDTO) throws NoElementException, ScoringException;
    void setSighDate(Long id) throws NoElementException;
}
