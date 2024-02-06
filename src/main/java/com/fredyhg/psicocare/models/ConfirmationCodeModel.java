package com.fredyhg.psicocare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_confirmation_code")
public class ConfirmationCodeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinTable(
            name = "code_therapy",
            joinColumns = @JoinColumn(name = "confirmation_code_id"),
            inverseJoinColumns = @JoinColumn(name = "therapy_id")
    )
    private TherapyModel therapy;

    private String code;

    private boolean used;
}
