package neostudy.dto;

import lombok.*;
import neostudy.entity.ApplicationStatusHistory;
import neostudy.entity.AppliedOffer;
import neostudy.entity.Client;
import neostudy.entity.Credit;
import neostudy.model.Status;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ApplicationDTO {
    private Long id;
    private Client client;
    private CreditDTO creditDTO;
    private Status status;
    private LocalDate creationDate;
    private AppliedOffer appliedOffer;
    private LocalDate signDate;
    private Integer sesCode;
    private List<ApplicationStatusHistoryDTO> applicationStatusHistoryDTOList;
}
