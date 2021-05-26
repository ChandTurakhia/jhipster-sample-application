package com.myapp.repository;

import com.myapp.domain.PersonDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonDetailsRepository extends JpaRepository<PersonDetails, Long> {}
