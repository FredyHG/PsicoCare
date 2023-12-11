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

    @ManyToOne
    @JoinTable(
            name = "therapy_patient",
            joinColumns = @JoinColumn(name = "therapy_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private PatientModel patient;

    @ManyToOne
    @JoinTable(
            name = "therapy_psychologist",
            joinColumns = @JoinColumn(name = "therapy_id"),
            inverseJoinColumns = @JoinColumn(name = "psychologist_id")
    )
    private PsychologistModel psychologist;

    @Enumerated(EnumType.STRING)
    private StatusTherapy status;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    public boolean isValidStatusForCancel(TherapyModel therapyModel){
        return therapyModel.getStatus() == StatusTherapy.WAIT_DATE;
    }

    public boolean isValidDateForSchedule(TherapyModel therapyModel){
        var now = LocalDateTime.now();
        var tomorrow = now.plus(Period.ofDays(1));

        var therapyDate = therapyModel.getDate();

        boolean isBeforeTomorrow = therapyDate.isBefore(tomorrow);

        var startTime = LocalTime.of(7, 0);
        var endTime = LocalTime.of(18, 0);

        return isBeforeTomorrow ||
                therapyDate.toLocalTime().isBefore(startTime) ||
                therapyDate.toLocalTime().isAfter(endTime);
    }

}
