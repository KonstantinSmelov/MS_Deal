package neostudy.entity;

import lombok.*;
import neostudy.CreditStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "credit", fetch = FetchType.EAGER)
    private List<PaymentSchedule> paymentScheduleList;

    @Column(name = "is_insurance_enabled")
    private Boolean isInsuranceEnabled;

    @Column(name = "is_salary_client")
    private Boolean isSalaryClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    private CreditStatus creditStatus;

    public void addScheduleElementToCredit(PaymentSchedule paymentSchedule) {
        if (paymentScheduleList == null) {
            paymentScheduleList = new ArrayList<>();
        }
        paymentScheduleList.add(paymentSchedule);
        paymentSchedule.setCredit(this);
    }

    public List<PaymentSchedule> getScheduleElementFromCredit() {
        return this.paymentScheduleList;
    }


}
