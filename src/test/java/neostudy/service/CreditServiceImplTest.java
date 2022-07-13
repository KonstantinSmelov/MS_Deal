package neostudy.service;

import neostudy.dao.CreditRepository;
import neostudy.entity.Client;
import neostudy.entity.Credit;
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
class CreditServiceImplTest {

    @InjectMocks
    private CreditServiceImpl creditService;

    @Mock
    private CreditRepository creditRepository;


    @Test
    void getCredit() throws NoElementException {
        when(creditRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Credit.builder().term(10).build()));

        Credit credit = creditService.getCredit(100L);

        assertEquals(10, credit.getTerm());
    }

    @Test
    void initCredit() {
        creditService.initCredit();

        verify(creditRepository, times(1)).save(any(Credit.class));
    }

    @Test
    void saveCredit() {

        creditService.saveCredit(Credit.builder().build());

        verify(creditRepository, times(1)).save(any(Credit.class));
    }
}