package neostudy.dto;

import lombok.Builder;
import lombok.Data;
import neostudy.Gender;
import neostudy.MaritalStatus;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestDTO {

    private Gender gender;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDTO employment;
    private String account;

}
