package com.passnail.server.core.app.repository;


import com.passnail.server.core.app.entity.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * A {@link org.springframework.stereotype.Repository} being a Data Access Object for the {@link CredentialsEntity} entity class.
 */
@Repository
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, UUID> {


}
