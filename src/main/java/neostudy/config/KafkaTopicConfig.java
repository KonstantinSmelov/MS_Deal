package neostudy.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic finishRegistrationTopic() {
        return TopicBuilder.name("finish-registration").build();
    }

    @Bean
    public NewTopic createDocTopic() {
        return TopicBuilder.name("create-documents").build();
    }

    @Bean
    public NewTopic sendDocTopic() {
        return TopicBuilder.name("send-documents").build();
    }

    @Bean
    public NewTopic sendSesTopic() {
        return TopicBuilder.name("send-ses").build();
    }

    @Bean
    public NewTopic creditIssuedTopic() {
        return TopicBuilder.name("credit-issued").build();
    }

    @Bean
    public NewTopic appDeniedTopic() {
        return TopicBuilder.name("application-denied").build();
    }
}
