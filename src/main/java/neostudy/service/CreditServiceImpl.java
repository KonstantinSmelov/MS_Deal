package neostudy.service;

import neostudy.dao.CreditRepository;
import neostudy.entity.Client;
import neostudy.entity.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public Credit getCredit(Long id) {
        Credit credit = null;

        Optional<Credit> optionalCredit = creditRepository.findById(id);
        if(optionalCredit.isPresent()) {
            credit = optionalCredit.get();
        }
        return credit;
    }

    @Override
    public void saveCredit(Credit credit) {
        creditRepository.save(credit);
    }
}
