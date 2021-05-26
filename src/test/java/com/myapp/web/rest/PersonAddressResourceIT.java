package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PersonAddress;
import com.myapp.repository.PersonAddressRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PersonAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonAddressResourceIT {

    private static final String DEFAULT_ADDRESS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_TYPE_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_VALID_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/person-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonAddressRepository personAddressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonAddressMockMvc;

    private PersonAddress personAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonAddress createEntity(EntityManager em) {
        PersonAddress personAddress = new PersonAddress()
            .addressTypeCode(DEFAULT_ADDRESS_TYPE_CODE)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO);
        return personAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonAddress createUpdatedEntity(EntityManager em) {
        PersonAddress personAddress = new PersonAddress()
            .addressTypeCode(UPDATED_ADDRESS_TYPE_CODE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);
        return personAddress;
    }

    @BeforeEach
    public void initTest() {
        personAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonAddress() throws Exception {
        int databaseSizeBeforeCreate = personAddressRepository.findAll().size();
        // Create the PersonAddress
        restPersonAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAddress)))
            .andExpect(status().isCreated());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PersonAddress testPersonAddress = personAddressList.get(personAddressList.size() - 1);
        assertThat(testPersonAddress.getAddressTypeCode()).isEqualTo(DEFAULT_ADDRESS_TYPE_CODE);
        assertThat(testPersonAddress.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testPersonAddress.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
    }

    @Test
    @Transactional
    void createPersonAddressWithExistingId() throws Exception {
        // Create the PersonAddress with an existing ID
        personAddress.setId(1L);

        int databaseSizeBeforeCreate = personAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAddress)))
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonAddresses() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        // Get all the personAddressList
        restPersonAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressTypeCode").value(hasItem(DEFAULT_ADDRESS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())));
    }

    @Test
    @Transactional
    void getPersonAddress() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        // Get the personAddress
        restPersonAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, personAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personAddress.getId().intValue()))
            .andExpect(jsonPath("$.addressTypeCode").value(DEFAULT_ADDRESS_TYPE_CODE))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonAddress() throws Exception {
        // Get the personAddress
        restPersonAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonAddress() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();

        // Update the personAddress
        PersonAddress updatedPersonAddress = personAddressRepository.findById(personAddress.getId()).get();
        // Disconnect from session so that the updates on updatedPersonAddress are not directly saved in db
        em.detach(updatedPersonAddress);
        updatedPersonAddress.addressTypeCode(UPDATED_ADDRESS_TYPE_CODE).validFrom(UPDATED_VALID_FROM).validTo(UPDATED_VALID_TO);

        restPersonAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonAddress))
            )
            .andExpect(status().isOk());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
        PersonAddress testPersonAddress = personAddressList.get(personAddressList.size() - 1);
        assertThat(testPersonAddress.getAddressTypeCode()).isEqualTo(UPDATED_ADDRESS_TYPE_CODE);
        assertThat(testPersonAddress.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testPersonAddress.getValidTo()).isEqualTo(UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void putNonExistingPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personAddress)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonAddressWithPatch() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();

        // Update the personAddress using partial update
        PersonAddress partialUpdatedPersonAddress = new PersonAddress();
        partialUpdatedPersonAddress.setId(personAddress.getId());

        partialUpdatedPersonAddress.validFrom(UPDATED_VALID_FROM).validTo(UPDATED_VALID_TO);

        restPersonAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonAddress))
            )
            .andExpect(status().isOk());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
        PersonAddress testPersonAddress = personAddressList.get(personAddressList.size() - 1);
        assertThat(testPersonAddress.getAddressTypeCode()).isEqualTo(DEFAULT_ADDRESS_TYPE_CODE);
        assertThat(testPersonAddress.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testPersonAddress.getValidTo()).isEqualTo(UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void fullUpdatePersonAddressWithPatch() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();

        // Update the personAddress using partial update
        PersonAddress partialUpdatedPersonAddress = new PersonAddress();
        partialUpdatedPersonAddress.setId(personAddress.getId());

        partialUpdatedPersonAddress.addressTypeCode(UPDATED_ADDRESS_TYPE_CODE).validFrom(UPDATED_VALID_FROM).validTo(UPDATED_VALID_TO);

        restPersonAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonAddress))
            )
            .andExpect(status().isOk());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
        PersonAddress testPersonAddress = personAddressList.get(personAddressList.size() - 1);
        assertThat(testPersonAddress.getAddressTypeCode()).isEqualTo(UPDATED_ADDRESS_TYPE_CODE);
        assertThat(testPersonAddress.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testPersonAddress.getValidTo()).isEqualTo(UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void patchNonExistingPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonAddress() throws Exception {
        int databaseSizeBeforeUpdate = personAddressRepository.findAll().size();
        personAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personAddress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonAddress in the database
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonAddress() throws Exception {
        // Initialize the database
        personAddressRepository.saveAndFlush(personAddress);

        int databaseSizeBeforeDelete = personAddressRepository.findAll().size();

        // Delete the personAddress
        restPersonAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, personAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonAddress> personAddressList = personAddressRepository.findAll();
        assertThat(personAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
