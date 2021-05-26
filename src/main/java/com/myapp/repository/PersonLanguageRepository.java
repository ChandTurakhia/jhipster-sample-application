package com.myapp.repository;

import com.myapp.domain.PersonLanguage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonLanguage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonLanguageRepository extends JpaRepository<PersonLanguage, Long> {}
