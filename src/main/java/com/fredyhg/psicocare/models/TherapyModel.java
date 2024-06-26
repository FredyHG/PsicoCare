package com.fredyhg.psicocare.models;

import com.fredyhg.psicocare.enums.StatusTherapy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_therapy")
public class TherapyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "patient.id"
    )
    private PatientModel patient;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "psychologist.id"
    )
    private PsychologistModel psychologist;

    @Enumerated(EnumType.STRING)
    private StatusTherapy status;

    @Column(name = "date")
    private LocalDateTime dateTime;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    private void onCreate(){
        createAt = LocalDateTime.now();
    }

    public boolean isValidStatusForChange(TherapyModel therapyModel){
        return therapyModel.getStatus() == StatusTherapy.FINISH;
    }

    public boolean isValidStatusForConfirm(TherapyModel therapyModel){
        return therapyModel.getStatus() == StatusTherapy.WAIT_CONFIRMATION;
    }

    public boolean isValidForCancel(TherapyModel therapyModel){
        return therapyModel.getStatus() == StatusTherapy.WAIT_CONFIRMATION || therapyModel.getStatus() == StatusTherapy.WAIT_DATE;
    }

    public boolean isValidDateForSchedule(TherapyModel therapyModel){
        var now = LocalDateTime.now();
        var tomorrow = now.plus(Period.ofDays(1));

        var therapyDate = therapyModel.getDateTime();

        boolean isBeforeTomorrow = therapyDate.isBefore(tomorrow);

        var startTime = LocalTime.of(7, 0);
        var endTime = LocalTime.of(18, 0);

        return isBeforeTomorrow ||
                therapyDate.toLocalTime().isBefore(startTime) ||
                therapyDate.toLocalTime().isAfter(endTime);
    }
}
