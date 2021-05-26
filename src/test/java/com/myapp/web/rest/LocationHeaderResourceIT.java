package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.LocationHeader;
import com.myapp.repository.LocationHeaderRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LocationHeaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationHeaderResourceIT {

    private static final Long DEFAULT_LATITUDE = 1L;
    private static final Long UPDATED_LATITUDE = 2L;

    private static final Long DEFAULT_LONGITUDE = 1L;
    private static final Long UPDATED_LONGITUDE = 2L;

    private static final Long DEFAULT_ELEVATION = 1L;
    private static final Long UPDATED_ELEVATION = 2L;

    private static final String ENTITY_API_URL = "/api/location-headers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationHeaderRepository locationHeaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationHeaderMockMvc;

    private LocationHeader locationHeader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationHeader createEntity(EntityManager em) {
        LocationHeader locationHeader = new LocationHeader()
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .elevation(DEFAULT_ELEVATION);
        return locationHeader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationHeader createUpdatedEntity(EntityManager em) {
        LocationHeader locationHeader = new LocationHeader()
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .elevation(UPDATED_ELEVATION);
        return locationHeader;
    }

    @BeforeEach
    public void initTest() {
        locationHeader = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationHeader() throws Exception {
        int databaseSizeBeforeCreate = locationHeaderRepository.findAll().size();
        // Create the LocationHeader
        restLocationHeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationHeader))
            )
            .andExpect(status().isCreated());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeCreate + 1);
        LocationHeader testLocationHeader = locationHeaderList.get(locationHeaderList.size() - 1);
        assertThat(testLocationHeader.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testLocationHeader.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testLocationHeader.getElevation()).isEqualTo(DEFAULT_ELEVATION);
    }

    @Test
    @Transactional
    void createLocationHeaderWithExistingId() throws Exception {
        // Create the LocationHeader with an existing ID
        locationHeader.setId(1L);

        int databaseSizeBeforeCreate = locationHeaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationHeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationHeaders() throws Exception {
        // Initialize the database
        locationHeaderRepository.saveAndFlush(locationHeader);

        // Get all the locationHeaderList
        restLocationHeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationHeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].elevation").value(hasItem(DEFAULT_ELEVATION.intValue())));
    }

    @Test
    @Transactional
    void getLocationHeader() throws Exception {
        // Initialize the database
        locationHeaderRepository.saveAndFlush(locationHeader);

        // Get the locationHeader
        restLocationHeaderMockMvc
            .perform(get(ENTITY_API_URL_ID, locationHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationHeader.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.elevation").value(DEFAULT_ELEVATION.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLocationHeader() throws Exception {
        // Get the locationHeader
        restLocationHeaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocationHeader() throws Exception {
        // Initialize the database
        locationHeaderRepository.saveAndFlush(locationHeader);

        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();

        // Update the locationHeader
        LocationHeader updatedLocationHeader = locationHeaderRepository.findById(locationHeader.getId()).get();
        // Disconnect from session so that the updates on updatedLocationHeader are not directly saved in db
        em.detach(updatedLocationHeader);
        updatedLocationHeader.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).elevation(UPDATED_ELEVATION);

        restLocationHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocationHeader))
            )
            .andExpect(status().isOk());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
        LocationHeader testLocationHeader = locationHeaderList.get(locationHeaderList.size() - 1);
        assertThat(testLocationHeader.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocationHeader.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocationHeader.getElevation()).isEqualTo(UPDATED_ELEVATION);
    }

    @Test
    @Transactional
    void putNonExistingLocationHeader() throws Exception {
        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();
        locationHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationHeader() throws Exception {
        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();
        locationHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationHeader() throws Exception {
        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();
        locationHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationHeaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationHeader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationHeaderWithPatch() throws Exception {
        // Initialize the database
        locationHeaderRepository.saveAndFlush(locationHeader);

        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();

        // Update the locationHeader using partial update
        LocationHeader partialUpdatedLocationHeader = new LocationHeader();
        partialUpdatedLocationHeader.setId(locationHeader.getId());

        partialUpdatedLocationHeader.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).elevation(UPDATED_ELEVATION);

        restLocationHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationHeader))
            )
            .andExpect(status().isOk());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
        LocationHeader testLocationHeader = locationHeaderList.get(locationHeaderList.size() - 1);
        assertThat(testLocationHeader.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocationHeader.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocationHeader.getElevation()).isEqualTo(UPDATED_ELEVATION);
    }

    @Test
    @Transactional
    void fullUpdateLocationHeaderWithPatch() throws Exception {
        // Initialize the database
        locationHeaderRepository.saveAndFlush(locationHeader);

        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();

        // Update the locationHeader using partial update
        LocationHeader partialUpdatedLocationHeader = new LocationHeader();
        partialUpdatedLocationHeader.setId(locationHeader.getId());

        partialUpdatedLocationHeader.latitude(UPDATED_LATITUDE).longitude(UPDATED_LONGITUDE).elevation(UPDATED_ELEVATION);

        restLocationHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationHeader))
            )
            .andExpect(status().isOk());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
        LocationHeader testLocationHeader = locationHeaderList.get(locationHeaderList.size() - 1);
        assertThat(testLocationHeader.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testLocationHeader.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testLocationHeader.getElevation()).isEqualTo(UPDATED_ELEVATION);
    }

    @Test
    @Transactional
    void patchNonExistingLocationHeader() throws Exception {
        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();
        locationHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationHeader() throws Exception {
        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();
        locationHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationHeader() throws Exception {
        int databaseSizeBeforeUpdate = locationHeaderRepository.findAll().size();
        locationHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(locationHeader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationHeader in the database
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationHeader() throws Exception {
        // Initialize the database
        locationHeaderRepository.saveAndFlush(locationHeader);

        int databaseSizeBeforeDelete = locationHeaderRepository.findAll().size();

        // Delete the locationHeader
        restLocationHeaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationHeader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationHeader> locationHeaderList = locationHeaderRepository.findAll();
        assertThat(locationHeaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
