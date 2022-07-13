package neostudy.service;

import neostudy.exception.NoElementException;
import neostudy.exception.SesCodeException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class SesCodeServiceTest {

    @InjectMocks
    private SesCodeService sesCodeService;

    @Test
    void generateSesCode() {
        assertEquals(4, sesCodeService.generateSesCode().toString().length());
    }

    @Test
    void checkSesCode() {
        try {
            sesCodeService.checkSesCode(1000, 1000, 1L);
        } catch (SesCodeException e) {
            e.printStackTrace();
        }

        assertThrows(SesCodeException.class, () -> sesCodeService.checkSesCode(1001,1000,1L));

    }
}