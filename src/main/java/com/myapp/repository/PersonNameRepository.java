package com.myapp.repository;

import com.myapp.domain.PersonName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonNameRepository extends JpaRepository<PersonName, Long> {}
