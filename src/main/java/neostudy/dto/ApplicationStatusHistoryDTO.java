package neostudy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import neostudy.model.ChangeType;
import neostudy.model.Status;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistoryDTO {

    private Status status;
    private LocalDate time;
    private ChangeType changeType;

}