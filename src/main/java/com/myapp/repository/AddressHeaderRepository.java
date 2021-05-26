package com.myapp.repository;

import com.myapp.domain.AddressHeader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AddressHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressHeaderRepository extends JpaRepository<AddressHeader, Long> {}
