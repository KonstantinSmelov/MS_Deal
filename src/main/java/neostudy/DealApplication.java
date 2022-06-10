package neostudy;

import neostudy.entity.Application;
import neostudy.service.ApplicationService;
import neostudy.service.ClientService;
import neostudy.service.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
public class DealApplication {

    public static void main(String[] args) {
        SpringApplication.run(DealApplication.class, args);
    }


}
