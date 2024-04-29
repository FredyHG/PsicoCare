package com.fredyhg.psicocare.repositories;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TherapyRepository extends JpaRepository<TherapyModel, UUID> {

    Optional<TherapyModel> findByPatientAndPsychologistAndStatus(PatientModel patient, PsychologistModel psychologist, StatusTherapy status);

    Optional<TherapyModel> findByDateTimeAndStatus(LocalDateTime date, StatusTherapy statusTherapy);

    Page<TherapyModel>findByStatus(StatusTherapy statusTherapy, Pageable pageable);

    @Query("SELECT t FROM TherapyModel t WHERE t.psychologist.id = :psychologistId AND t.status = 'WAIT_DATE' AND t.dateTime > :minDate AND t.dateTime < :maxDate")
    List<TherapyModel> findAllByPsychologistAndMinDateMaxDate(@Param("psychologistId") UUID psychologistId,
                                                              @Param("minDate") LocalDateTime minDate,
                                                              @Param("maxDate") LocalDateTime maxDate );

//    @Query(value = """
//                SELECT *
//                FROM tb_therapy t
//                JOIN tb_psychologist psy ON t.psychologist_id = psy.id
//                JOIN tb_patient pt ON t.patient_id = pt.id
//                WHERE
//                    (LOWER(pt.name) LIKE LOWER(CONCAT(:patientName, '%')) OR :patientName IS NULL)
//                    AND (LOWER(pt.cpf) LIKE LOWER(CONCAT(:patientCPF, '%')) OR :patientCPF IS NULL)
//                    AND (LOWER(psy.name) LIKE LOWER(CONCAT(:psychologistName, '%')) OR :psychologistName IS NULL)
//                    AND (LOWER(psy.crp) LIKE LOWER(CONCAT(:psychologistCRP, '%')) OR :psychologistCRP IS NULL)
//            """, nativeQuery = true)
//    Page<TherapyModel> findAllFiltered(@Param("patientName") String patientName,
//                                       @Param("patientCPF") String patientCPF,
//                                       @Param("psychologistName") String psychologistName,
//                                       @Param("psychologistCRP") String psychologistCRP, Pageable pageable);

    @Query("SELECT t FROM TherapyModel t " +
            "JOIN t.patient pat " +
            "JOIN t.psychologist psy " +
            "WHERE (:cpfPatient IS NULL OR pat.cpf = :cpfPatient) " +
            "AND (:crpPsychologist IS NULL OR psy.crp = :crpPsychologist)"+
            "AND (:statusTherapy IS NULL OR t.status = :statusTherapy)"+
            "AND t.dateTime BETWEEN  :startDate AND :endDate")
    Page<TherapyModel> findAllFiltered(@Param("cpfPatient") String patientCPF,
                                       @Param("crpPsychologist") String crpPsychologist,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("statusTherapy") StatusTherapy statusTherapy,
                                       Pageable pageable);


    Optional<TherapyModel> findByPatient(PatientModel patient);
}
