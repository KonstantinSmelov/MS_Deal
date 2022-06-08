package neostudy.service;

import neostudy.dao.AppliedOfferRepository;
import neostudy.entity.Application;
import neostudy.entity.AppliedOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppliedOfferServiceImpl implements AppliedOfferService {

    private final AppliedOfferRepository appliedOfferRepository;

    public AppliedOfferServiceImpl(AppliedOfferRepository appliedOfferRepository) {
        this.appliedOfferRepository = appliedOfferRepository;
    }

    @Override
    public AppliedOffer getAppliedOffer(Long id) {

        AppliedOffer appliedOffer = null;

        Optional<AppliedOffer> optionalAppliedOffer = appliedOfferRepository.findById(id);
        if(optionalAppliedOffer.isPresent()) {
            appliedOffer = optionalAppliedOffer.get();
        }

        return appliedOffer;
    }

    @Override
    public void saveAppliedOffer(AppliedOffer appliedOffer) {
        appliedOfferRepository.save(appliedOffer);
    }
}
