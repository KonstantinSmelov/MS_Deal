package neostudy.service;

import neostudy.entity.AppliedOffer;

public interface AppliedOfferService {
    AppliedOffer getAppliedOffer(Long id);
    void saveAppliedOffer(AppliedOffer appliedOffer);
}
