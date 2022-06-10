package neostudy.service;

import lombok.RequiredArgsConstructor;
import neostudy.dao.AppliedOfferRepository;
import neostudy.entity.Application;
import neostudy.entity.AppliedOffer;
import neostudy.exception.NoElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppliedOfferServiceImpl implements AppliedOfferService {

    private final AppliedOfferRepository appliedOfferRepository;

    @Override
    public AppliedOffer getAppliedOffer(Long id) throws NoElementException {

        return appliedOfferRepository.findById(id).orElseThrow(() -> new NoElementException("Принятого предложения с таким номером в БД нет"));

    }

    @Override
    public void saveAppliedOffer(AppliedOffer appliedOffer) {
        appliedOfferRepository.save(appliedOffer);
    }
}
