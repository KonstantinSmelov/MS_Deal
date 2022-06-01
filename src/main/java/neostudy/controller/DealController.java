package neostudy.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deal")
public class DealController {

    @PostMapping("/application")
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO) {
        return null;
    }
}
