package neostudy.dto;

import lombok.Builder;
import lombok.Data;
import neostudy.entity.Employment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SummaryAppInfo {
    private String fullName;
    private LocalDate birthdate;
    private String gender;
    private String fullPassportData;
    private String email;
    private String martialStatus;
    private Integer dependentAmount;
    private Employment employment;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentScheduleElementList;
    private Integer sesCode;
}
