package com.myapp.web.rest;

import com.myapp.domain.LocationHeader;
import com.myapp.repository.LocationHeaderRepository;
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
 * REST controller for managing {@link com.myapp.domain.LocationHeader}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LocationHeaderResource {

    private final Logger log = LoggerFactory.getLogger(LocationHeaderResource.class);

    private static final String ENTITY_NAME = "locationHeader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationHeaderRepository locationHeaderRepository;

    public LocationHeaderResource(LocationHeaderRepository locationHeaderRepository) {
        this.locationHeaderRepository = locationHeaderRepository;
    }

    /**
     * {@code POST  /location-headers} : Create a new locationHeader.
     *
     * @param locationHeader the locationHeader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationHeader, or with status {@code 400 (Bad Request)} if the locationHeader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-headers")
    public ResponseEntity<LocationHeader> createLocationHeader(@RequestBody LocationHeader locationHeader) throws URISyntaxException {
        log.debug("REST request to save LocationHeader : {}", locationHeader);
        if (locationHeader.getId() != null) {
            throw new BadRequestAlertException("A new locationHeader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationHeader result = locationHeaderRepository.save(locationHeader);
        return ResponseEntity
            .created(new URI("/api/location-headers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-headers/:id} : Updates an existing locationHeader.
     *
     * @param id the id of the locationHeader to save.
     * @param locationHeader the locationHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationHeader,
     * or with status {@code 400 (Bad Request)} if the locationHeader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-headers/{id}")
    public ResponseEntity<LocationHeader> updateLocationHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationHeader locationHeader
    ) throws URISyntaxException {
        log.debug("REST request to update LocationHeader : {}, {}", id, locationHeader);
        if (locationHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationHeader result = locationHeaderRepository.save(locationHeader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationHeader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-headers/:id} : Partial updates given fields of an existing locationHeader, field will ignore if it is null
     *
     * @param id the id of the locationHeader to save.
     * @param locationHeader the locationHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationHeader,
     * or with status {@code 400 (Bad Request)} if the locationHeader is not valid,
     * or with status {@code 404 (Not Found)} if the locationHeader is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-headers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocationHeader> partialUpdateLocationHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationHeader locationHeader
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationHeader partially : {}, {}", id, locationHeader);
        if (locationHeader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationHeader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationHeaderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationHeader> result = locationHeaderRepository
            .findById(locationHeader.getId())
            .map(
                existingLocationHeader -> {
                    if (locationHeader.getLatitude() != null) {
                        existingLocationHeader.setLatitude(locationHeader.getLatitude());
                    }
                    if (locationHeader.getLongitude() != null) {
                        existingLocationHeader.setLongitude(locationHeader.getLongitude());
                    }
                    if (locationHeader.getElevation() != null) {
                        existingLocationHeader.setElevation(locationHeader.getElevation());
                    }

                    return existingLocationHeader;
                }
            )
            .map(locationHeaderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationHeader.getId().toString())
        );
    }

    /**
     * {@code GET  /location-headers} : get all the locationHeaders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationHeaders in body.
     */
    @GetMapping("/location-headers")
    public List<LocationHeader> getAllLocationHeaders() {
        log.debug("REST request to get all LocationHeaders");
        return locationHeaderRepository.findAll();
    }

    /**
     * {@code GET  /location-headers/:id} : get the "id" locationHeader.
     *
     * @param id the id of the locationHeader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationHeader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-headers/{id}")
    public ResponseEntity<LocationHeader> getLocationHeader(@PathVariable Long id) {
        log.debug("REST request to get LocationHeader : {}", id);
        Optional<LocationHeader> locationHeader = locationHeaderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(locationHeader);
    }

    /**
     * {@code DELETE  /location-headers/:id} : delete the "id" locationHeader.
     *
     * @param id the id of the locationHeader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-headers/{id}")
    public ResponseEntity<Void> deleteLocationHeader(@PathVariable Long id) {
        log.debug("REST request to delete LocationHeader : {}", id);
        locationHeaderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
