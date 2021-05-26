package com.myapp.web.rest;

import com.myapp.domain.PersonName;
import com.myapp.repository.PersonNameRepository;
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
 * REST controller for managing {@link com.myapp.domain.PersonName}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonNameResource {

    private final Logger log = LoggerFactory.getLogger(PersonNameResource.class);

    private static final String ENTITY_NAME = "personName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonNameRepository personNameRepository;

    public PersonNameResource(PersonNameRepository personNameRepository) {
        this.personNameRepository = personNameRepository;
    }

    /**
     * {@code POST  /person-names} : Create a new personName.
     *
     * @param personName the personName to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personName, or with status {@code 400 (Bad Request)} if the personName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-names")
    public ResponseEntity<PersonName> createPersonName(@RequestBody PersonName personName) throws URISyntaxException {
        log.debug("REST request to save PersonName : {}", personName);
        if (personName.getId() != null) {
            throw new BadRequestAlertException("A new personName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonName result = personNameRepository.save(personName);
        return ResponseEntity
            .created(new URI("/api/person-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-names/:id} : Updates an existing personName.
     *
     * @param id the id of the personName to save.
     * @param personName the personName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personName,
     * or with status {@code 400 (Bad Request)} if the personName is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-names/{id}")
    public ResponseEntity<PersonName> updatePersonName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonName personName
    ) throws URISyntaxException {
        log.debug("REST request to update PersonName : {}, {}", id, personName);
        if (personName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personName.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonName result = personNameRepository.save(personName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personName.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-names/:id} : Partial updates given fields of an existing personName, field will ignore if it is null
     *
     * @param id the id of the personName to save.
     * @param personName the personName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personName,
     * or with status {@code 400 (Bad Request)} if the personName is not valid,
     * or with status {@code 404 (Not Found)} if the personName is not found,
     * or with status {@code 500 (Internal Server Error)} if the personName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-names/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonName> partialUpdatePersonName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonName personName
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonName partially : {}, {}", id, personName);
        if (personName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personName.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonName> result = personNameRepository
            .findById(personName.getId())
            .map(
                existingPersonName -> {
                    if (personName.getPersonId() != null) {
                        existingPersonName.setPersonId(personName.getPersonId());
                    }
                    if (personName.getFirstName() != null) {
                        existingPersonName.setFirstName(personName.getFirstName());
                    }
                    if (personName.getMiddleName() != null) {
                        existingPersonName.setMiddleName(personName.getMiddleName());
                    }
                    if (personName.getLastName() != null) {
                        existingPersonName.setLastName(personName.getLastName());
                    }
                    if (personName.getSecondLastName() != null) {
                        existingPersonName.setSecondLastName(personName.getSecondLastName());
                    }
                    if (personName.getPreferredName() != null) {
                        existingPersonName.setPreferredName(personName.getPreferredName());
                    }
                    if (personName.getPrefixCode() != null) {
                        existingPersonName.setPrefixCode(personName.getPrefixCode());
                    }
                    if (personName.getSuffixCode() != null) {
                        existingPersonName.setSuffixCode(personName.getSuffixCode());
                    }
                    if (personName.getValidFrom() != null) {
                        existingPersonName.setValidFrom(personName.getValidFrom());
                    }
                    if (personName.getValidTo() != null) {
                        existingPersonName.setValidTo(personName.getValidTo());
                    }

                    return existingPersonName;
                }
            )
            .map(personNameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personName.getId().toString())
        );
    }

    /**
     * {@code GET  /person-names} : get all the personNames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personNames in body.
     */
    @GetMapping("/person-names")
    public List<PersonName> getAllPersonNames() {
        log.debug("REST request to get all PersonNames");
        return personNameRepository.findAll();
    }

    /**
     * {@code GET  /person-names/:id} : get the "id" personName.
     *
     * @param id the id of the personName to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personName, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-names/{id}")
    public ResponseEntity<PersonName> getPersonName(@PathVariable Long id) {
        log.debug("REST request to get PersonName : {}", id);
        Optional<PersonName> personName = personNameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personName);
    }

    /**
     * {@code DELETE  /person-names/:id} : delete the "id" personName.
     *
     * @param id the id of the personName to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-names/{id}")
    public ResponseEntity<Void> deletePersonName(@PathVariable Long id) {
        log.debug("REST request to delete PersonName : {}", id);
        personNameRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
