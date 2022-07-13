package neostudy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import neostudy.dto.EmailMessage;
import neostudy.entity.Application;
import neostudy.entity.Client;
import neostudy.exception.NoElementException;
import neostudy.model.Theme;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class KafkaProducerServiceTest {

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ApplicationService applicationService;

    @Test
    void send() throws JsonProcessingException, NoElementException {

        EmailMessage emailMessage = EmailMessage.builder()
                .address("test")
                .applicationId(100L)
                .theme(Theme.CREATE_DOCUMENTS).build();

        Application application = Application.builder().client(Client.builder().build()).build();

        when(applicationService.getApplication(anyLong())).thenReturn(application);
        when(objectMapper.writeValueAsString(emailMessage)).thenReturn("test");

        kafkaProducerService.send("test", Theme.CREATE_DOCUMENTS, 100L);

        verify(objectMapper, times(1)).writeValueAsString(any(EmailMessage.class));

    }
}