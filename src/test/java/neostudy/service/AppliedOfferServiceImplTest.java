package neostudy.service;

import neostudy.dao.AppliedOfferRepository;
import neostudy.entity.Application;
import neostudy.entity.AppliedOffer;
import neostudy.exception.NoElementException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class AppliedOfferServiceImplTest {

    @InjectMocks
    private AppliedOfferServiceImpl appliedOfferService;

    @Mock
    private AppliedOfferRepository appliedOfferRepository;

    @Test
    void getAppliedOffer() throws NoElementException {

        when(appliedOfferRepository.findById(1L)).thenReturn((Optional.of(AppliedOffer.builder().term(10).build())));

        AppliedOffer appliedOffer = appliedOfferService.getAppliedOffer(1L);

        assertEquals(10, appliedOffer.getTerm());
    }

    @Test
    void saveAppliedOffer() {

        AppliedOffer appliedOffer = AppliedOffer.builder().term(10).build();

        appliedOfferService.saveAppliedOffer(appliedOffer);

        verify(appliedOfferRepository, times(1)).save(any(AppliedOffer.class));
    }
}