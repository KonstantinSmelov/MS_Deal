package neostudy.service;

import neostudy.dao.ApplicationRepository;
import neostudy.dao.AppliedOfferRepository;
import neostudy.dto.LoanApplicationRequestDTO;
import neostudy.dto.LoanOfferDTO;
import neostudy.entity.Application;
import neostudy.entity.AppliedOffer;
import neostudy.entity.Client;
import neostudy.exception.NoElementException;
import neostudy.feignclient.ConveyorClient;
import neostudy.model.ChangeType;
import neostudy.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private CreditService creditService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ConveyorClient conveyorClient;

    @Mock
    private SesCodeService sesCodeService;

    @Test
    public void getApplication() throws NoElementException {
        when(applicationRepository.findById(anyLong())).thenReturn(Optional.of(Application.builder().sesCode(1111).build()));

        Application application = applicationService.getApplication(1L);

        assertEquals(1111, application.getSesCode());
    }

    @Test
    public void getApplicationWithException() {
        try {
            when(applicationService.getApplication(anyLong())).thenThrow(NoElementException.class);
        } catch (NoElementException e) {
            e.printStackTrace();
        }

        assertThrows(NoElementException.class, () -> applicationService.getApplication(1L));

    }

    @Test
    void getAllApplications() {
        Application app1 = Application.builder().sesCode(1111).build();
        Application app2 = Application.builder().sesCode(2222).build();
        Application app3 = Application.builder().sesCode(3333).build();
        Application app4 = Application.builder().sesCode(4444).build();

        when(applicationRepository.findAll()).thenReturn(List.of(
                Application.builder().sesCode(1111).build(),
                Application.builder().sesCode(2222).build(),
                Application.builder().sesCode(3333).build(),
                Application.builder().sesCode(4444).build()));

        List<Application> apps = applicationService.getAllApplications();

        assertEquals(List.of(app1, app2, app3, app4), apps);

    }

    @Test
    void initApplication() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder().build();

        when(clientService.initClient(LoanApplicationRequestDTO.builder().build())).thenReturn(Client.builder().dependentAmount(10).build());

        Application application = applicationService.initApplication(loanApplicationRequestDTO);

        verify(clientService, times(1)).initClient(any(LoanApplicationRequestDTO.class));
        verify(creditService, times(1)).initCredit();
        verify(applicationRepository, times(1)).save(any(Application.class));
        assertEquals(10, application.getClient().getDependentAmount());
    }

    @Test
    void saveApplication() {
        Application application = Application.builder().build();

        applicationService.saveApplication(application);

        verify(applicationRepository, times(1)).save(any(Application.class));

    }
}