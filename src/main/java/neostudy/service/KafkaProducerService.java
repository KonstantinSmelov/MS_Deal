package neostudy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dto.EmailMessage;
import neostudy.entity.Application;
import neostudy.exception.NoElementException;
import neostudy.model.Theme;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationService applicationService;

    public void send(String topic, Theme theme, Long id) throws JsonProcessingException, NoElementException {

        Application app = applicationService.getApplication(id);
        log.debug("send(): достали из БД application: {}", app);

        EmailMessage emailMessage = EmailMessage.builder()
                .address(app.getClient().getEmail())
                .theme(theme)
                .applicationId(app.getId())
                .build();
        log.debug("send(): сформировали EmailMessage: {}", emailMessage);

        String json = objectMapper.writeValueAsString(emailMessage);
        log.debug("send(): перевели EmailMessage в JSON для передачи в Кафку: {}", json);

        kafkaTemplate.send(topic, json);
    }

}
