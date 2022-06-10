package neostudy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import neostudy.model.EmploymentStatus;
import neostudy.model.Position;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDTO {

    @ApiModelProperty(notes = "Занятость")
    private EmploymentStatus employmentStatus;

    @ApiModelProperty(notes = "ИНН")
    private String employerINN;

    @ApiModelProperty(notes = "ЗП")
    private BigDecimal salary;

    @ApiModelProperty(notes = "Должность")
    private Position position;

    @ApiModelProperty(notes = "Общий трудовой стаж")
    private Integer workExperienceTotal;

    @ApiModelProperty(notes = "Стаж на текущем месте работы")
    private Integer workExperienceCurrent;
}
