package com.myapp.web.rest;

import com.myapp.domain.PersonAddress;
import com.myapp.repository.PersonAddressRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.PersonAddress}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonAddressResource {

    private final Logger log = LoggerFactory.getLogger(PersonAddressResource.class);

    private static final String ENTITY_NAME = "personAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonAddressRepository personAddressRepository;

    public PersonAddressResource(PersonAddressRepository personAddressRepository) {
        this.personAddressRepository = personAddressRepository;
    }

    /**
     * {@code POST  /person-addresses} : Create a new personAddress.
     *
     * @param personAddress the personAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personAddress, or with status {@code 400 (Bad Request)} if the personAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-addresses")
    public ResponseEntity<PersonAddress> createPersonAddress(@RequestBody PersonAddress personAddress) throws URISyntaxException {
        log.debug("REST request to save PersonAddress : {}", personAddress);
        if (personAddress.getId() != null) {
            throw new BadRequestAlertException("A new personAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonAddress result = personAddressRepository.save(personAddress);
        return ResponseEntity
            .created(new URI("/api/person-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-addresses/:id} : Updates an existing personAddress.
     *
     * @param id the id of the personAddress to save.
     * @param personAddress the personAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personAddress,
     * or with status {@code 400 (Bad Request)} if the personAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-addresses/{id}")
    public ResponseEntity<PersonAddress> updatePersonAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonAddress personAddress
    ) throws URISyntaxException {
        log.debug("REST request to update PersonAddress : {}, {}", id, personAddress);
        if (personAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonAddress result = personAddressRepository.save(personAddress);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personAddress.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-addresses/:id} : Partial updates given fields of an existing personAddress, field will ignore if it is null
     *
     * @param id the id of the personAddress to save.
     * @param personAddress the personAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personAddress,
     * or with status {@code 400 (Bad Request)} if the personAddress is not valid,
     * or with status {@code 404 (Not Found)} if the personAddress is not found,
     * or with status {@code 500 (Internal Server Error)} if the personAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-addresses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonAddress> partialUpdatePersonAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonAddress personAddress
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonAddress partially : {}, {}", id, personAddress);
        if (personAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonAddress> result = personAddressRepository
            .findById(personAddress.getId())
            .map(
                existingPersonAddress -> {
                    if (personAddress.getAddressTypeCode() != null) {
                        existingPersonAddress.setAddressTypeCode(personAddress.getAddressTypeCode());
                    }
                    if (personAddress.getValidFrom() != null) {
                        existingPersonAddress.setValidFrom(personAddress.getValidFrom());
                    }
                    if (personAddress.getValidTo() != null) {
                        existingPersonAddress.setValidTo(personAddress.getValidTo());
                    }

                    return existingPersonAddress;
                }
            )
            .map(personAddressRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personAddress.getId().toString())
        );
    }

    /**
     * {@code GET  /person-addresses} : get all the personAddresses.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personAddresses in body.
     */
    @GetMapping("/person-addresses")
    public List<PersonAddress> getAllPersonAddresses(@RequestParam(required = false) String filter) {
        if ("addressheader-is-null".equals(filter)) {
            log.debug("REST request to get all PersonAddresss where addressHeader is null");
            return StreamSupport
                .stream(personAddressRepository.findAll().spliterator(), false)
                .filter(personAddress -> personAddress.getAddressHeader() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all PersonAddresses");
        return personAddressRepository.findAll();
    }

    /**
     * {@code GET  /person-addresses/:id} : get the "id" personAddress.
     *
     * @param id the id of the personAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-addresses/{id}")
    public ResponseEntity<PersonAddress> getPersonAddress(@PathVariable Long id) {
        log.debug("REST request to get PersonAddress : {}", id);
        Optional<PersonAddress> personAddress = personAddressRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personAddress);
    }

    /**
     * {@code DELETE  /person-addresses/:id} : delete the "id" personAddress.
     *
     * @param id the id of the personAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-addresses/{id}")
    public ResponseEntity<Void> deletePersonAddress(@PathVariable Long id) {
        log.debug("REST request to delete PersonAddress : {}", id);
        personAddressRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
