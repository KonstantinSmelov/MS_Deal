package neostudy.service;

import neostudy.entity.Credit;
import neostudy.exception.NoElementException;

public interface CreditService {
    Credit getCredit(Long id) throws NoElementException;
    void saveCredit(Credit credit);
    Credit initCredit();

}
