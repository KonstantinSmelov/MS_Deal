package neostudy.service;

import neostudy.entity.AppliedOffer;
import neostudy.exception.NoElementException;

public interface AppliedOfferService {
    AppliedOffer getAppliedOffer(Long id) throws NoElementException;
    void saveAppliedOffer(AppliedOffer appliedOffer);
}
