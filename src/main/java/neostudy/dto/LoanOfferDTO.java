package neostudy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Data
@Builder
public class LoanOfferDTO {

    @ApiModelProperty(notes = "Номер предложения")
    private Long applicationId;

    @ApiModelProperty(notes = "Запрашиваемая сумма в кредит")
    private BigDecimal requestedAmount;

    @ApiModelProperty(notes = "Итоговый кредит")
    private BigDecimal totalAmount;

    @ApiModelProperty(notes = "Срок кредита в месяцах")
    private Integer term;

    @ApiModelProperty(notes = "Ежемесячный платёж")
    private BigDecimal monthlyPayment;

    @ApiModelProperty(notes = "Годовая ставка")
    private BigDecimal rate;

    @ApiModelProperty(notes = "Страховка включена?")
    private Boolean isInsuranceEnabled;

    @ApiModelProperty(notes = "Зарплатный клиент?")
    private Boolean isSalaryClient;
}
