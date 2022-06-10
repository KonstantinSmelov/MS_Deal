package neostudy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanApplicationRequestDTO {

    @ApiModelProperty(notes = "Сумма кредита")
    private BigDecimal amount;

    @ApiModelProperty(notes = "Срок кредита")
    private Integer term;

    @ApiModelProperty(notes = "Имя")
    private String firstName;

    @ApiModelProperty(notes = "Фамилия")
    private String lastName;

    @ApiModelProperty(notes = "Отчёство")
    private String middleName;

    @ApiModelProperty(notes = "Email")
    private String email;

    @ApiModelProperty(notes = "День рождения")
    private LocalDate birthdate;

    @ApiModelProperty(notes = "Серия паспорта")
    private String passportSeries;

    @ApiModelProperty(notes = "Номер паспорта")
    private String passportNumber;
}
