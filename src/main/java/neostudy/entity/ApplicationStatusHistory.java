package neostudy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import neostudy.model.ChangeType;
import neostudy.model.Status;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "application_status_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "time")
    private LocalDate time;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type")
    private ChangeType changeType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    private Application application;

}
