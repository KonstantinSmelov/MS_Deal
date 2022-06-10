package neostudy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import neostudy.model.Gender;
import neostudy.model.MaritalStatus;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestDTO {

    @ApiModelProperty(notes = "Пол")
    private Gender gender;

    @ApiModelProperty(notes = "Дата выдачи паспорта")
    private LocalDate passportIssueDate;

    @ApiModelProperty(notes = "Отделение, которое выдало паспорт")
    private String passportIssueBranch;

    @ApiModelProperty(notes = "Семейный статус")
    private MaritalStatus maritalStatus;

    @ApiModelProperty(notes = "Кол-во иждивенцев")
    private Integer dependentAmount;

    @ApiModelProperty(notes = "Трудоустройство")
    private EmploymentDTO employment;

    @ApiModelProperty(notes = "Номер счёта")
    private String account;

}
