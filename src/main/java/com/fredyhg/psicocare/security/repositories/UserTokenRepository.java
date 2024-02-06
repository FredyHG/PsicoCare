package com.fredyhg.psicocare.security.repositories;

import com.fredyhg.psicocare.security.models.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTokenRepository extends JpaRepository<UserToken, UUID> {

    @Query(value = """
    select t from UserToken t inner join UserModel u\s
    on t.user.id = u.id \s
    where u.id = :id and (t.expired = false or t.revoked = false)
    """)
    List<UserToken> findAllValidTokenByUser(UUID id);

    Optional<UserToken> findByToken(String jwt);
}
