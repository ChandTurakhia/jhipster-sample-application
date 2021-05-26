package com.myapp.repository;

import com.myapp.domain.PersonAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonAddressRepository extends JpaRepository<PersonAddress, Long> {}
