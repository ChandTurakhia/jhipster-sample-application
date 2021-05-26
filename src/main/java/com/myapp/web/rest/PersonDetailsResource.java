package com.myapp.web.rest;

import com.myapp.domain.PersonDetails;
import com.myapp.repository.PersonDetailsRepository;
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
 * REST controller for managing {@link com.myapp.domain.PersonDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PersonDetailsResource.class);

    private static final String ENTITY_NAME = "personDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonDetailsRepository personDetailsRepository;

    public PersonDetailsResource(PersonDetailsRepository personDetailsRepository) {
        this.personDetailsRepository = personDetailsRepository;
    }

    /**
     * {@code POST  /person-details} : Create a new personDetails.
     *
     * @param personDetails the personDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personDetails, or with status {@code 400 (Bad Request)} if the personDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-details")
    public ResponseEntity<PersonDetails> createPersonDetails(@RequestBody PersonDetails personDetails) throws URISyntaxException {
        log.debug("REST request to save PersonDetails : {}", personDetails);
        if (personDetails.getId() != null) {
            throw new BadRequestAlertException("A new personDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonDetails result = personDetailsRepository.save(personDetails);
        return ResponseEntity
            .created(new URI("/api/person-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-details/:id} : Updates an existing personDetails.
     *
     * @param id the id of the personDetails to save.
     * @param personDetails the personDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personDetails,
     * or with status {@code 400 (Bad Request)} if the personDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-details/{id}")
    public ResponseEntity<PersonDetails> updatePersonDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonDetails personDetails
    ) throws URISyntaxException {
        log.debug("REST request to update PersonDetails : {}, {}", id, personDetails);
        if (personDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonDetails result = personDetailsRepository.save(personDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-details/:id} : Partial updates given fields of an existing personDetails, field will ignore if it is null
     *
     * @param id the id of the personDetails to save.
     * @param personDetails the personDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personDetails,
     * or with status {@code 400 (Bad Request)} if the personDetails is not valid,
     * or with status {@code 404 (Not Found)} if the personDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the personDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonDetails> partialUpdatePersonDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonDetails personDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonDetails partially : {}, {}", id, personDetails);
        if (personDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonDetails> result = personDetailsRepository
            .findById(personDetails.getId())
            .map(
                existingPersonDetails -> {
                    if (personDetails.getMaritalTypeStatusCode() != null) {
                        existingPersonDetails.setMaritalTypeStatusCode(personDetails.getMaritalTypeStatusCode());
                    }
                    if (personDetails.getRaceEthinicityCode() != null) {
                        existingPersonDetails.setRaceEthinicityCode(personDetails.getRaceEthinicityCode());
                    }
                    if (personDetails.getCitizenshipStatusCode() != null) {
                        existingPersonDetails.setCitizenshipStatusCode(personDetails.getCitizenshipStatusCode());
                    }
                    if (personDetails.getPregnant() != null) {
                        existingPersonDetails.setPregnant(personDetails.getPregnant());
                    }
                    if (personDetails.getChildrenCount() != null) {
                        existingPersonDetails.setChildrenCount(personDetails.getChildrenCount());
                    }
                    if (personDetails.getHeight() != null) {
                        existingPersonDetails.setHeight(personDetails.getHeight());
                    }
                    if (personDetails.getWeight() != null) {
                        existingPersonDetails.setWeight(personDetails.getWeight());
                    }

                    return existingPersonDetails;
                }
            )
            .map(personDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /person-details} : get all the personDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personDetails in body.
     */
    @GetMapping("/person-details")
    public List<PersonDetails> getAllPersonDetails() {
        log.debug("REST request to get all PersonDetails");
        return personDetailsRepository.findAll();
    }

    /**
     * {@code GET  /person-details/:id} : get the "id" personDetails.
     *
     * @param id the id of the personDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-details/{id}")
    public ResponseEntity<PersonDetails> getPersonDetails(@PathVariable Long id) {
        log.debug("REST request to get PersonDetails : {}", id);
        Optional<PersonDetails> personDetails = personDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personDetails);
    }

    /**
     * {@code DELETE  /person-details/:id} : delete the "id" personDetails.
     *
     * @param id the id of the personDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-details/{id}")
    public ResponseEntity<Void> deletePersonDetails(@PathVariable Long id) {
        log.debug("REST request to delete PersonDetails : {}", id);
        personDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
