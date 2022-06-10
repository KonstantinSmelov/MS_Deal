package neostudy.service;

import neostudy.exception.NoElementException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;

    @Test
    void getApplicationExceptionTestWithMock() {

        try {
            when(applicationService.getApplication(anyLong())).thenThrow(new NoElementException(""));
            assertThrows(Exception.class, () -> applicationService.getApplication(10000L));

        } catch (NoElementException e) {
            System.out.println(e.getMessage());
        }
    }
}