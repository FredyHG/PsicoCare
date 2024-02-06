package com.fredyhg.psicocare.models;

import com.fredyhg.psicocare.security.models.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_psychologist")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsychologistModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "crp")
    private String crp;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "create_at")
    private LocalDateTime createAt;

}
