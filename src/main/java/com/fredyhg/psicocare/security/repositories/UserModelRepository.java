package com.fredyhg.psicocare.security.repositories;

import com.fredyhg.psicocare.security.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserModelRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByUsername(String username);

}
