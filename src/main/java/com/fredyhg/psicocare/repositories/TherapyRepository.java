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


}
