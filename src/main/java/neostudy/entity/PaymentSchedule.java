package neostudy.entity;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "payment_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @Column(name = "interest_payment")
    private BigDecimal interestPayment;

    @Column(name = "debt_payment")
    private BigDecimal debtPayment;

    @Column(name = "remaining_debt")
    private BigDecimal remainingDebt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id")
    private Credit credit;

}
