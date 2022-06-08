package neostudy.service;

import neostudy.entity.Credit;

public interface CreditService {
    Credit getCredit(Long id);
    void saveCredit(Credit credit);
}
