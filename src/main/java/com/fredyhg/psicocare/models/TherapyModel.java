package com.fredyhg.psicocare.models;

import com.fredyhg.psicocare.enums.StatusTherapy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "therapy_id")
    )
    private PatientModel patient;

    @ManyToOne
    @JoinTable(
            name = "therapy_psychologist",
            joinColumns = @JoinColumn(name = "psychologist_id"),
            inverseJoinColumns = @JoinColumn(name = "therapy_id")
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
}
