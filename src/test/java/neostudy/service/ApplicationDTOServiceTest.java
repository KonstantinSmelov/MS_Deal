package neostudy.service;

import liquibase.pro.packaged.L;
import neostudy.dto.ApplicationDTO;
import neostudy.dto.ApplicationStatusHistoryDTO;
import neostudy.entity.Application;
import neostudy.entity.ApplicationStatusHistory;
import neostudy.exception.NoElementException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ApplicationDTOServiceTest {

    @InjectMocks
    private ApplicationDTOService applicationDTOService;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void getAppDTO() throws NoElementException {

        Application application = Application.builder()
                .id(1L)
                .applicationStatusHistoryList(List.of(new ApplicationStatusHistory()))
                .build();

        when(applicationService.getApplication(anyLong())).thenReturn(application);

        ApplicationDTO applicationDTO = applicationDTOService.getAppDTO(1L);

        verify(modelMapper, times(application.getApplicationStatusHistoryList().size() + 1)).map(any(), any());
        assertEquals(applicationDTO.getId(), application.getId());
    }

    @Test
    void getAllAppDTO() throws NoElementException {

        Application application = Application.builder()
                .id(111L)
                .applicationStatusHistoryList(List.of(new ApplicationStatusHistory()))
                .build();

        List<Application> applicationList = List.of(application);

        when(applicationService.getApplication(anyLong())).thenReturn(application);
        when(applicationService.getAllApplications()).thenReturn(applicationList);

        List<ApplicationDTO> applicationDTOList = applicationDTOService.getAllAppDTO();

        assertEquals(applicationDTOList.get(0).getId(), applicationList.get(0).getId());

    }
}