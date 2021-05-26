package com.myapp.web.rest;

import com.myapp.domain.PersonLanguage;
import com.myapp.repository.PersonLanguageRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.PersonLanguage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonLanguageResource {

    private final Logger log = LoggerFactory.getLogger(PersonLanguageResource.class);

    private static final String ENTITY_NAME = "personLanguage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonLanguageRepository personLanguageRepository;

    public PersonLanguageResource(PersonLanguageRepository personLanguageRepository) {
        this.personLanguageRepository = personLanguageRepository;
    }

    /**
     * {@code POST  /person-languages} : Create a new personLanguage.
     *
     * @param personLanguage the personLanguage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personLanguage, or with status {@code 400 (Bad Request)} if the personLanguage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-languages")
    public ResponseEntity<PersonLanguage> createPersonLanguage(@RequestBody PersonLanguage personLanguage) throws URISyntaxException {
        log.debug("REST request to save PersonLanguage : {}", personLanguage);
        if (personLanguage.getId() != null) {
            throw new BadRequestAlertException("A new personLanguage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonLanguage result = personLanguageRepository.save(personLanguage);
        return ResponseEntity
            .created(new URI("/api/person-languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-languages/:id} : Updates an existing personLanguage.
     *
     * @param id the id of the personLanguage to save.
     * @param personLanguage the personLanguage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personLanguage,
     * or with status {@code 400 (Bad Request)} if the personLanguage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personLanguage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-languages/{id}")
    public ResponseEntity<PersonLanguage> updatePersonLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonLanguage personLanguage
    ) throws URISyntaxException {
        log.debug("REST request to update PersonLanguage : {}, {}", id, personLanguage);
        if (personLanguage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personLanguage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personLanguageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonLanguage result = personLanguageRepository.save(personLanguage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personLanguage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-languages/:id} : Partial updates given fields of an existing personLanguage, field will ignore if it is null
     *
     * @param id the id of the personLanguage to save.
     * @param personLanguage the personLanguage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personLanguage,
     * or with status {@code 400 (Bad Request)} if the personLanguage is not valid,
     * or with status {@code 404 (Not Found)} if the personLanguage is not found,
     * or with status {@code 500 (Internal Server Error)} if the personLanguage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-languages/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonLanguage> partialUpdatePersonLanguage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonLanguage personLanguage
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonLanguage partially : {}, {}", id, personLanguage);
        if (personLanguage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personLanguage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personLanguageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonLanguage> result = personLanguageRepository
            .findById(personLanguage.getId())
            .map(
                existingPersonLanguage -> {
                    if (personLanguage.getLanguageCode() != null) {
                        existingPersonLanguage.setLanguageCode(personLanguage.getLanguageCode());
                    }
                    if (personLanguage.getLanguageUsageCode() != null) {
                        existingPersonLanguage.setLanguageUsageCode(personLanguage.getLanguageUsageCode());
                    }
                    if (personLanguage.getPreferredLanguage() != null) {
                        existingPersonLanguage.setPreferredLanguage(personLanguage.getPreferredLanguage());
                    }

                    return existingPersonLanguage;
                }
            )
            .map(personLanguageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personLanguage.getId().toString())
        );
    }

    /**
     * {@code GET  /person-languages} : get all the personLanguages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personLanguages in body.
     */
    @GetMapping("/person-languages")
    public List<PersonLanguage> getAllPersonLanguages() {
        log.debug("REST request to get all PersonLanguages");
        return personLanguageRepository.findAll();
    }

    /**
     * {@code GET  /person-languages/:id} : get the "id" personLanguage.
     *
     * @param id the id of the personLanguage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personLanguage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-languages/{id}")
    public ResponseEntity<PersonLanguage> getPersonLanguage(@PathVariable Long id) {
        log.debug("REST request to get PersonLanguage : {}", id);
        Optional<PersonLanguage> personLanguage = personLanguageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personLanguage);
    }

    /**
     * {@code DELETE  /person-languages/:id} : delete the "id" personLanguage.
     *
     * @param id the id of the personLanguage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-languages/{id}")
    public ResponseEntity<Void> deletePersonLanguage(@PathVariable Long id) {
        log.debug("REST request to delete PersonLanguage : {}", id);
        personLanguageRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
