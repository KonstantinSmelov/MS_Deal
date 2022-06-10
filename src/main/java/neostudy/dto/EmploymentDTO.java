package neostudy.dto;

import lombok.Builder;
import lombok.Data;
import neostudy.model.EmploymentStatus;
import neostudy.model.Position;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
