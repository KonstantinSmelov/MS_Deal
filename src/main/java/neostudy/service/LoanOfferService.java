package neostudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.dto.LoanOfferDTO;
import neostudy.entity.Application;
import neostudy.exception.ScoringException;
import neostudy.feignclient.ConveyorClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class LoanOfferService {

    private final ConveyorClient conveyorClient;
    private final ApplicationService applicationService;

    public List<LoanOfferDTO> creatingLoanOfferDTOList(LoanApplicationRequestDTO loanApplicationRequestDTO) throws ScoringException {

        List<LoanOfferDTO> loanOfferDTOList = conveyorClient.getLoanOfferListFromConveyor(loanApplicationRequestDTO);

        if (loanOfferDTOList.get(0).getApplicationId() == null) {
            throw new ScoringException("Скоринг не пройден!");
        }

        Application application = applicationService.initApplication(loanApplicationRequestDTO);

        log.debug("creatingLoanOfferDTOList(): Получен loanOfferDTOList из Conveyor: {}", loanOfferDTOList);

        loanOfferDTOList.forEach(s -> s.setApplicationId(application.getId()));
        log.debug("creatingLoanOfferDTOList(): return loanOfferDTOList: {}", loanOfferDTOList);

        return loanOfferDTOList;
    }
}
