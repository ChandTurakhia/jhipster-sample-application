package com.myapp.web.rest;

import com.myapp.domain.AddressHeader;
import com.myapp.repository.AddressHeaderRepository;
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
 * REST controller for managing {@link com.myapp.domain.AddressHeader}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AddressHeaderResource {

    private final Logger log = LoggerFactory.getLogger(AddressHeaderResource.class);

    private static final String ENTITY_NAME = "addressHeader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AddressHeaderRepository addressHeaderRepository;

    public AddressHeaderResource(AddressHeaderRepository addressHeaderRepository) {
        this.addressHeaderRepository = addressHeaderRepository;
    }

    /**
     * {@code POST  /address-headers} : Create a new addressHeader.
     *
     * @param addressHeader the addressHeader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new addressHeader, or with status {@code 400 (Bad Request)} if the addressHeader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/address-headers")
    public ResponseEntity<AddressHeader> createAddressHeader(@RequestBody AddressHeader addressHeader) throws URISyntaxException {
        log.debug("REST request to save AddressHeader : {}", addressHeader);
        if (addressHeader.getId() != null) {
            throw new BadRequestAlertException("A new addressHeader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddressHeader result = addressHeaderRepository.save(addressHeader);
        return ResponseEntity
            .created(new URI("/api/address-headers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /address-headers/:id} : Updates an existing addressHeader.
     *
     * @param id the id of the addressHeader to save.
     * @param addressHeader the addressHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressHeader,
     * or with status {@code 400 (Bad Request)} if the addressHeader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the addressHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/address-headers/{id}")
    public ResponseEntity<AddressHeader> updateAddressHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddressHeader addressHeader
    ) throws URISyntaxException {
        log.debug("REST request to update AddressHeader : {}, {}", id, addressHeader);
        if (addressHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addressHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addressHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AddressHeader result = addressHeaderRepository.save(addressHeader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addressHeader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /address-headers/:id} : Partial updates given fields of an existing addressHeader, field will ignore if it is null
     *
     * @param id the id of the addressHeader to save.
     * @param addressHeader the addressHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressHeader,
     * or with status {@code 400 (Bad Request)} if the addressHeader is not valid,
     * or with status {@code 404 (Not Found)} if the addressHeader is not found,
     * or with status {@code 500 (Internal Server Error)} if the addressHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/address-headers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AddressHeader> partialUpdateAddressHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddressHeader addressHeader
    ) throws URISyntaxException {
        log.debug("REST request to partial update AddressHeader partially : {}, {}", id, addressHeader);
        if (addressHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addressHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addressHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AddressHeader> result = addressHeaderRepository
            .findById(addressHeader.getId())
            .map(
                existingAddressHeader -> {
                    if (addressHeader.getTypeCode() != null) {
                        existingAddressHeader.setTypeCode(addressHeader.getTypeCode());
                    }
                    if (addressHeader.getStandardized() != null) {
                        existingAddressHeader.setStandardized(addressHeader.getStandardized());
                    }
                    if (addressHeader.getAddressLine1() != null) {
                        existingAddressHeader.setAddressLine1(addressHeader.getAddressLine1());
                    }
                    if (addressHeader.getAddressLine2() != null) {
                        existingAddressHeader.setAddressLine2(addressHeader.getAddressLine2());
                    }
                    if (addressHeader.getAddressLine3() != null) {
                        existingAddressHeader.setAddressLine3(addressHeader.getAddressLine3());
                    }
                    if (addressHeader.getCityName() != null) {
                        existingAddressHeader.setCityName(addressHeader.getCityName());
                    }
                    if (addressHeader.getCountyName() != null) {
                        existingAddressHeader.setCountyName(addressHeader.getCountyName());
                    }
                    if (addressHeader.getStateCode() != null) {
                        existingAddressHeader.setStateCode(addressHeader.getStateCode());
                    }
                    if (addressHeader.getZipCode() != null) {
                        existingAddressHeader.setZipCode(addressHeader.getZipCode());
                    }
                    if (addressHeader.getCountryName() != null) {
                        existingAddressHeader.setCountryName(addressHeader.getCountryName());
                    }

                    return existingAddressHeader;
                }
            )
            .map(addressHeaderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addressHeader.getId().toString())
        );
    }

    /**
     * {@code GET  /address-headers} : get all the addressHeaders.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addressHeaders in body.
     */
    @GetMapping("/address-headers")
    public List<AddressHeader> getAllAddressHeaders(@RequestParam(required = false) String filter) {
        if ("locationheader-is-null".equals(filter)) {
            log.debug("REST request to get all AddressHeaders where locationHeader is null");
            return StreamSupport
                .stream(addressHeaderRepository.findAll().spliterator(), false)
                .filter(addressHeader -> addressHeader.getLocationHeader() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all AddressHeaders");
        return addressHeaderRepository.findAll();
    }

    /**
     * {@code GET  /address-headers/:id} : get the "id" addressHeader.
     *
     * @param id the id of the addressHeader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addressHeader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/address-headers/{id}")
    public ResponseEntity<AddressHeader> getAddressHeader(@PathVariable Long id) {
        log.debug("REST request to get AddressHeader : {}", id);
        Optional<AddressHeader> addressHeader = addressHeaderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(addressHeader);
    }

    /**
     * {@code DELETE  /address-headers/:id} : delete the "id" addressHeader.
     *
     * @param id the id of the addressHeader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/address-headers/{id}")
    public ResponseEntity<Void> deleteAddressHeader(@PathVariable Long id) {
        log.debug("REST request to delete AddressHeader : {}", id);
        addressHeaderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
