package neostudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neostudy.dto.PaymentScheduleElement;
import neostudy.dto.SummaryAppInfo;
import neostudy.exception.NoElementException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryAppInfoService {

    private final ApplicationService as;
    private final ModelMapper modelMapper;

    public SummaryAppInfo getSummaryAppInfo(Long id) throws NoElementException {

        List<PaymentScheduleElement> paymentScheduleElementList = new ArrayList<>();

        for(int x = 0; x < as.getApplication(id).getCredit().getPaymentScheduleList().size(); x++) {
            paymentScheduleElementList.add(modelMapper.map(as.getApplication(id).getCredit().getPaymentScheduleList().get(x), PaymentScheduleElement.class));
        }

        SummaryAppInfo summaryAppInfo = SummaryAppInfo.builder()
                .fullName(as.getApplication(id).getClient().getFirstName()
                        + " " + as.getApplication(id).getClient().getMiddleName()
                        + " " + as.getApplication(id).getClient().getLastName())
                .birthdate(as.getApplication(id).getClient().getBirthdate())
                .gender(as.getApplication(id).getClient().getGender().toString())
                .fullPassportData(as.getApplication(id).getClient().getPassport().getPassportNumber()
                        + " " + as.getApplication(id).getClient().getPassport().getPassportSeries()
                        + " выдан " + as.getApplication(id).getClient().getPassport().getIssueBranch()
                        + " " + as.getApplication(id).getClient().getPassport().getIssueDate())
                .email(as.getApplication(id).getClient().getEmail())
                .dependentAmount(as.getApplication(id).getClient().getDependentAmount())
                .martialStatus(as.getApplication(id).getClient().getMaritalStatus().toString())
                .employment(as.getApplication(id).getClient().getEmployment())
                .amount(as.getApplication(id).getCredit().getAmount())
                .term(as.getApplication(id).getCredit().getTerm())
                .monthlyPayment(as.getApplication(id).getCredit().getMonthlyPayment())
                .rate(as.getApplication(id).getCredit().getRate())
                .psk(as.getApplication(id).getCredit().getPsk())
                .isInsuranceEnabled(as.getApplication(id).getCredit().getIsInsuranceEnabled())
                .isSalaryClient(as.getApplication(id).getCredit().getIsSalaryClient())
                .paymentScheduleElementList(paymentScheduleElementList)
                .sesCode(as.getApplication(id).getSesCode())
                .build();
        log.debug("getSummaryAppInfo(): сущность SummaryAppInfo заполнена: {}", summaryAppInfo);

        return summaryAppInfo;
    }
}
