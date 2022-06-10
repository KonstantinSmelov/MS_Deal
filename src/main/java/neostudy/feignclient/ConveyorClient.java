package neostudy.feignclient;

import neostudy.dto.CreditDTO;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.dto.LoanOfferDTO;
import neostudy.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="conveyor-service-client", url = "${URL.toConveyor}")
public interface ConveyorClient {

    @PostMapping("/conveyor/offers")
    List<LoanOfferDTO> getLoanOfferListFromConveyor(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/conveyor/calculation")
    CreditDTO getScoringDataFromConveyor(@RequestBody ScoringDataDTO scoringDataDTO);
}
