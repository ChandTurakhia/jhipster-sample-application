package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.AddressHeader;
import com.myapp.repository.AddressHeaderRepository;
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
 * Integration tests for the {@link AddressHeaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressHeaderResourceIT {

    private static final String DEFAULT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STANDARDIZED = false;
    private static final Boolean UPDATED_STANDARDIZED = true;

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/address-headers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressHeaderRepository addressHeaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressHeaderMockMvc;

    private AddressHeader addressHeader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressHeader createEntity(EntityManager em) {
        AddressHeader addressHeader = new AddressHeader()
            .typeCode(DEFAULT_TYPE_CODE)
            .standardized(DEFAULT_STANDARDIZED)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .addressLine3(DEFAULT_ADDRESS_LINE_3)
            .cityName(DEFAULT_CITY_NAME)
            .countyName(DEFAULT_COUNTY_NAME)
            .stateCode(DEFAULT_STATE_CODE)
            .zipCode(DEFAULT_ZIP_CODE)
            .countryName(DEFAULT_COUNTRY_NAME);
        return addressHeader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressHeader createUpdatedEntity(EntityManager em) {
        AddressHeader addressHeader = new AddressHeader()
            .typeCode(UPDATED_TYPE_CODE)
            .standardized(UPDATED_STANDARDIZED)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .addressLine3(UPDATED_ADDRESS_LINE_3)
            .cityName(UPDATED_CITY_NAME)
            .countyName(UPDATED_COUNTY_NAME)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE)
            .countryName(UPDATED_COUNTRY_NAME);
        return addressHeader;
    }

    @BeforeEach
    public void initTest() {
        addressHeader = createEntity(em);
    }

    @Test
    @Transactional
    void createAddressHeader() throws Exception {
        int databaseSizeBeforeCreate = addressHeaderRepository.findAll().size();
        // Create the AddressHeader
        restAddressHeaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressHeader)))
            .andExpect(status().isCreated());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeCreate + 1);
        AddressHeader testAddressHeader = addressHeaderList.get(addressHeaderList.size() - 1);
        assertThat(testAddressHeader.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testAddressHeader.getStandardized()).isEqualTo(DEFAULT_STANDARDIZED);
        assertThat(testAddressHeader.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testAddressHeader.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testAddressHeader.getAddressLine3()).isEqualTo(DEFAULT_ADDRESS_LINE_3);
        assertThat(testAddressHeader.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
        assertThat(testAddressHeader.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testAddressHeader.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
        assertThat(testAddressHeader.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testAddressHeader.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void createAddressHeaderWithExistingId() throws Exception {
        // Create the AddressHeader with an existing ID
        addressHeader.setId(1L);

        int databaseSizeBeforeCreate = addressHeaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressHeaderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressHeader)))
            .andExpect(status().isBadRequest());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAddressHeaders() throws Exception {
        // Initialize the database
        addressHeaderRepository.saveAndFlush(addressHeader);

        // Get all the addressHeaderList
        restAddressHeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressHeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].standardized").value(hasItem(DEFAULT_STANDARDIZED.booleanValue())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].addressLine3").value(hasItem(DEFAULT_ADDRESS_LINE_3)))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));
    }

    @Test
    @Transactional
    void getAddressHeader() throws Exception {
        // Initialize the database
        addressHeaderRepository.saveAndFlush(addressHeader);

        // Get the addressHeader
        restAddressHeaderMockMvc
            .perform(get(ENTITY_API_URL_ID, addressHeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(addressHeader.getId().intValue()))
            .andExpect(jsonPath("$.typeCode").value(DEFAULT_TYPE_CODE))
            .andExpect(jsonPath("$.standardized").value(DEFAULT_STANDARDIZED.booleanValue()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.addressLine3").value(DEFAULT_ADDRESS_LINE_3))
            .andExpect(jsonPath("$.cityName").value(DEFAULT_CITY_NAME))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME))
            .andExpect(jsonPath("$.stateCode").value(DEFAULT_STATE_CODE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAddressHeader() throws Exception {
        // Get the addressHeader
        restAddressHeaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAddressHeader() throws Exception {
        // Initialize the database
        addressHeaderRepository.saveAndFlush(addressHeader);

        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();

        // Update the addressHeader
        AddressHeader updatedAddressHeader = addressHeaderRepository.findById(addressHeader.getId()).get();
        // Disconnect from session so that the updates on updatedAddressHeader are not directly saved in db
        em.detach(updatedAddressHeader);
        updatedAddressHeader
            .typeCode(UPDATED_TYPE_CODE)
            .standardized(UPDATED_STANDARDIZED)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .addressLine3(UPDATED_ADDRESS_LINE_3)
            .cityName(UPDATED_CITY_NAME)
            .countyName(UPDATED_COUNTY_NAME)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE)
            .countryName(UPDATED_COUNTRY_NAME);

        restAddressHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAddressHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAddressHeader))
            )
            .andExpect(status().isOk());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
        AddressHeader testAddressHeader = addressHeaderList.get(addressHeaderList.size() - 1);
        assertThat(testAddressHeader.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testAddressHeader.getStandardized()).isEqualTo(UPDATED_STANDARDIZED);
        assertThat(testAddressHeader.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testAddressHeader.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testAddressHeader.getAddressLine3()).isEqualTo(UPDATED_ADDRESS_LINE_3);
        assertThat(testAddressHeader.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testAddressHeader.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testAddressHeader.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
        assertThat(testAddressHeader.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAddressHeader.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAddressHeader() throws Exception {
        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();
        addressHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressHeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddressHeader() throws Exception {
        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();
        addressHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressHeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddressHeader() throws Exception {
        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();
        addressHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressHeaderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressHeader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressHeaderWithPatch() throws Exception {
        // Initialize the database
        addressHeaderRepository.saveAndFlush(addressHeader);

        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();

        // Update the addressHeader using partial update
        AddressHeader partialUpdatedAddressHeader = new AddressHeader();
        partialUpdatedAddressHeader.setId(addressHeader.getId());

        partialUpdatedAddressHeader
            .typeCode(UPDATED_TYPE_CODE)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .addressLine3(UPDATED_ADDRESS_LINE_3)
            .cityName(UPDATED_CITY_NAME)
            .countyName(UPDATED_COUNTY_NAME)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE)
            .countryName(UPDATED_COUNTRY_NAME);

        restAddressHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressHeader))
            )
            .andExpect(status().isOk());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
        AddressHeader testAddressHeader = addressHeaderList.get(addressHeaderList.size() - 1);
        assertThat(testAddressHeader.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testAddressHeader.getStandardized()).isEqualTo(DEFAULT_STANDARDIZED);
        assertThat(testAddressHeader.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testAddressHeader.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testAddressHeader.getAddressLine3()).isEqualTo(UPDATED_ADDRESS_LINE_3);
        assertThat(testAddressHeader.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testAddressHeader.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testAddressHeader.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
        assertThat(testAddressHeader.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAddressHeader.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAddressHeaderWithPatch() throws Exception {
        // Initialize the database
        addressHeaderRepository.saveAndFlush(addressHeader);

        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();

        // Update the addressHeader using partial update
        AddressHeader partialUpdatedAddressHeader = new AddressHeader();
        partialUpdatedAddressHeader.setId(addressHeader.getId());

        partialUpdatedAddressHeader
            .typeCode(UPDATED_TYPE_CODE)
            .standardized(UPDATED_STANDARDIZED)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .addressLine3(UPDATED_ADDRESS_LINE_3)
            .cityName(UPDATED_CITY_NAME)
            .countyName(UPDATED_COUNTY_NAME)
            .stateCode(UPDATED_STATE_CODE)
            .zipCode(UPDATED_ZIP_CODE)
            .countryName(UPDATED_COUNTRY_NAME);

        restAddressHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressHeader))
            )
            .andExpect(status().isOk());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
        AddressHeader testAddressHeader = addressHeaderList.get(addressHeaderList.size() - 1);
        assertThat(testAddressHeader.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testAddressHeader.getStandardized()).isEqualTo(UPDATED_STANDARDIZED);
        assertThat(testAddressHeader.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testAddressHeader.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testAddressHeader.getAddressLine3()).isEqualTo(UPDATED_ADDRESS_LINE_3);
        assertThat(testAddressHeader.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testAddressHeader.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testAddressHeader.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
        assertThat(testAddressHeader.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAddressHeader.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAddressHeader() throws Exception {
        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();
        addressHeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressHeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddressHeader() throws Exception {
        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();
        addressHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressHeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddressHeader() throws Exception {
        int databaseSizeBeforeUpdate = addressHeaderRepository.findAll().size();
        addressHeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressHeaderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addressHeader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AddressHeader in the database
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddressHeader() throws Exception {
        // Initialize the database
        addressHeaderRepository.saveAndFlush(addressHeader);

        int databaseSizeBeforeDelete = addressHeaderRepository.findAll().size();

        // Delete the addressHeader
        restAddressHeaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, addressHeader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AddressHeader> addressHeaderList = addressHeaderRepository.findAll();
        assertThat(addressHeaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
