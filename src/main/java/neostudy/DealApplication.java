package neostudy;

import neostudy.entity.Employment;
import neostudy.entity.PaymentSchedule;
import neostudy.service.ClientService;
import neostudy.service.CreditService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
public class DealApplication /*implements CommandLineRunner*/ {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(DealApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//
//        List<PaymentSchedule> paymentScheduleList = creditService.getCredit(3L).getPaymentScheduleList();
//
//        System.out.println(paymentScheduleList);
//
//    }
}
