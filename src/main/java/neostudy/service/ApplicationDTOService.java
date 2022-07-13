package neostudy.service;

import lombok.RequiredArgsConstructor;
import neostudy.dto.ApplicationDTO;
import neostudy.dto.ApplicationStatusHistoryDTO;
import neostudy.dto.CreditDTO;
import neostudy.entity.Application;
import neostudy.exception.NoElementException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationDTOService {

    private final ApplicationService as;
    private final ModelMapper modelMapper;

    public ApplicationDTO getAppDTO(Long id) throws NoElementException {

        List<ApplicationStatusHistoryDTO> applicationStatusHistoryDTOList = new ArrayList<>();

        for (int x = 0; x < as.getApplication(id).getApplicationStatusHistoryList().size(); x++) {
            applicationStatusHistoryDTOList.add(modelMapper.map(as.getApplication(id).getApplicationStatusHistoryList().get(x), ApplicationStatusHistoryDTO.class));
        }

        return ApplicationDTO.builder()
                .id(as.getApplication(id).getId())
                .client(as.getApplication(id).getClient())
                .creditDTO(modelMapper.map(as.getApplication(id).getCredit(), CreditDTO.class))
                .status(as.getApplication(id).getStatus())
                .creationDate(as.getApplication(id).getCreationDate())
                .appliedOffer(as.getApplication(id).getAppliedOffer())
                .signDate(as.getApplication(id).getSignDate())
                .sesCode(as.getApplication(id).getSesCode())
                .applicationStatusHistoryDTOList(applicationStatusHistoryDTOList)
                .build();
    }

    public List<ApplicationDTO> getAllAppDTO() throws NoElementException {
        List<ApplicationDTO> applicationDTOList = new ArrayList<>();
        List<Application> applicationList = as.getAllApplications();

        for (long x = 0L; x < applicationList.size(); x++) {
            applicationDTOList.add(getAppDTO(x + 1));
        }
        return applicationDTOList;
    }
}
