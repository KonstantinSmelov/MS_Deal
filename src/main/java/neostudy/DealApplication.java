package neostudy;

import neostudy.service.SesCodeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients
public class DealApplication {

    public static void main(String[] args) {
        SpringApplication.run(DealApplication.class, args);
    }

}
