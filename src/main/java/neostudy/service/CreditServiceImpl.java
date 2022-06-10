package neostudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dao.CreditRepository;
import neostudy.entity.Credit;
import neostudy.entity.PaymentSchedule;
import neostudy.exception.NoElementException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit getCredit(Long id) throws NoElementException {
        Credit credit = creditRepository.findById(id).orElseThrow(() -> new NoElementException("Кредита с таким номером в БД нет"));
        log.debug("getCredit(): из БД взят Credit с id {}: {}", id, credit);
        return credit;
    }

    @Override
    public Credit initCredit() {
        List<PaymentSchedule> paymentScheduleList = new ArrayList<>();
        Credit credit = Credit.builder().paymentScheduleList(paymentScheduleList).build();
        creditRepository.save(credit);
        log.debug("initCredit(): в БД инициализирован Credit: {}", credit);
        return credit;
    }

    @Override
    public void saveCredit(Credit credit) {
        creditRepository.save(credit);
        log.debug("saveCredit(): в БД сохранён Credit: {}", credit);
    }
}
