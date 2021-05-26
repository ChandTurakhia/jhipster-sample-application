package com.myapp.repository;

import com.myapp.domain.LocationHeader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationHeaderRepository extends JpaRepository<LocationHeader, Long> {}
