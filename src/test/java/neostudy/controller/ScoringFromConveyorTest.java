package neostudy.controller;

import neostudy.dao.ApplicationRepository;
import neostudy.entity.*;
import neostudy.model.Status;
import neostudy.service.ApplicationService;
import neostudy.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ScoringFromConveyorTest {

    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getApplicationWrongDtoTest() throws Exception {

        String json = "{\n" +
                "  \"amount\": 0,\n" +  //неверный запрос
                "  \"birthdate\": \"2000-06-06\",\n" +
                "  \"email\": \"strin@gmail.ru\",\n" +
                "  \"firstName\": \"sdfg\",\n" +
                "  \"lastName\": \"sdfg\",\n" +
                "  \"middleName\": \"dfgs\",\n" +
                "  \"passportNumber\": \"123565\",\n" +
                "  \"passportSeries\": \"1234\",\n" +
                "  \"term\": 8\n" +
                "}";

        mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(451));
    }

    @Test
    void getApplicationCorrectDtoTest() throws Exception {

        String json = "{\n" +
                "  \"amount\": 65000,\n" +
                "  \"birthdate\": \"2000-06-06\",\n" +
                "  \"email\": \"string@mail.ru\",\n" +
                "  \"firstName\": \"string\",\n" +
                "  \"lastName\": \"string\",\n" +
                "  \"middleName\": \"string\",\n" +
                "  \"passportNumber\": \"123456\",\n" +
                "  \"passportSeries\": \"1234\",\n" +
                "  \"term\": 20\n" +
                "}";

        mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void choosingOfferCorrect() throws Exception {

        String json = "{\n" +
                "  \"applicationId\": 1,\n" +
                "  \"requestedAmount\": 65000,\n" +
                "  \"totalAmount\": 65000,\n" +
                "  \"term\": 20,\n" +
                "  \"monthlyPayment\": 3662.74,\n" +
                "  \"rate\": 14,\n" +
                "  \"isInsuranceEnabled\": false,\n" +
                "  \"isSalaryClient\": true\n" +
                "}\n";

        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(Application.builder().build()));

        mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}