package com.fredyhg.psicocare.repositories;

import com.fredyhg.psicocare.models.PatientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<PatientModel, UUID> {

    Optional<PatientModel>findByCpf(String cpf);

    Optional<PatientModel>findByEmail(String email);


    @Query(value = """
                SELECT *
                FROM tb_patient tp\s
                WHERE\s
                    (LOWER(tp.name) LIKE LOWER(CONCAT(:name, '%')) OR :name IS NULL)
                    AND (LOWER(tp.last_name) LIKE LOWER(CONCAT(:last_name, '%')) OR :last_name IS NULL)
                    AND (LOWER(tp.cpf) LIKE LOWER(CONCAT(:cpf, '%')) OR :cpf IS NULL)
                    AND (LOWER(tp.email) LIKE LOWER(CONCAT(:email, '%')) OR :email IS NULL)
            """, nativeQuery = true)
    Page<PatientModel> findAllFiltered(@Param("name") String name,
                                       @Param("last_name") String lastName,
                                       @Param("cpf") String cpf,
                                       @Param("email") String email, Pageable pageable);
}
