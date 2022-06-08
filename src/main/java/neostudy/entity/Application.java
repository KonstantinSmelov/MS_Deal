package neostudy.entity;

import lombok.*;
import neostudy.Status;
import neostudy.dto.ApplicationStatusHistoryDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applied_offer_id")
    private AppliedOffer appliedOffer;

    @Column(name = "sign_date")
    private LocalDate signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "application")
    private List<ApplicationStatusHistory> applicationStatusHistoriesList;

    public void addStatusHistoryToApp(ApplicationStatusHistory applicationStatusHistory) {
        if(applicationStatusHistoriesList == null) {
            applicationStatusHistoriesList = new ArrayList<>();
        }
        applicationStatusHistoriesList.add(applicationStatusHistory);
        applicationStatusHistory.setApplication(this);
    }
}
