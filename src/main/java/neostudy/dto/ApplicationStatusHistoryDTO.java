package neostudy.dto;

import lombok.Builder;
import lombok.Data;
import neostudy.model.ChangeType;
import neostudy.model.Status;

import java.time.LocalDate;

@Data
@Builder
public class ApplicationStatusHistoryDTO {

    private Status status;
    private LocalDate time;
    private ChangeType changeType;

}
