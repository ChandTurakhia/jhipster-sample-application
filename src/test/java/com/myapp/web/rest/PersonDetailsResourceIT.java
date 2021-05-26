package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PersonDetails;
import com.myapp.repository.PersonDetailsRepository;
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
 * Integration tests for the {@link PersonDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonDetailsResourceIT {

    private static final String DEFAULT_MARITAL_TYPE_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MARITAL_TYPE_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RACE_ETHINICITY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RACE_ETHINICITY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITIZENSHIP_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CITIZENSHIP_STATUS_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PREGNANT = false;
    private static final Boolean UPDATED_PREGNANT = true;

    private static final Long DEFAULT_CHILDREN_COUNT = 1L;
    private static final Long UPDATED_CHILDREN_COUNT = 2L;

    private static final String DEFAULT_HEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_HEIGHT = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final String ENTITY_API_URL = "/api/person-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonDetailsRepository personDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonDetailsMockMvc;

    private PersonDetails personDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonDetails createEntity(EntityManager em) {
        PersonDetails personDetails = new PersonDetails()
            .maritalTypeStatusCode(DEFAULT_MARITAL_TYPE_STATUS_CODE)
            .raceEthinicityCode(DEFAULT_RACE_ETHINICITY_CODE)
            .citizenshipStatusCode(DEFAULT_CITIZENSHIP_STATUS_CODE)
            .pregnant(DEFAULT_PREGNANT)
            .childrenCount(DEFAULT_CHILDREN_COUNT)
            .height(DEFAULT_HEIGHT)
            .weight(DEFAULT_WEIGHT);
        return personDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonDetails createUpdatedEntity(EntityManager em) {
        PersonDetails personDetails = new PersonDetails()
            .maritalTypeStatusCode(UPDATED_MARITAL_TYPE_STATUS_CODE)
            .raceEthinicityCode(UPDATED_RACE_ETHINICITY_CODE)
            .citizenshipStatusCode(UPDATED_CITIZENSHIP_STATUS_CODE)
            .pregnant(UPDATED_PREGNANT)
            .childrenCount(UPDATED_CHILDREN_COUNT)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT);
        return personDetails;
    }

    @BeforeEach
    public void initTest() {
        personDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonDetails() throws Exception {
        int databaseSizeBeforeCreate = personDetailsRepository.findAll().size();
        // Create the PersonDetails
        restPersonDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDetails)))
            .andExpect(status().isCreated());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PersonDetails testPersonDetails = personDetailsList.get(personDetailsList.size() - 1);
        assertThat(testPersonDetails.getMaritalTypeStatusCode()).isEqualTo(DEFAULT_MARITAL_TYPE_STATUS_CODE);
        assertThat(testPersonDetails.getRaceEthinicityCode()).isEqualTo(DEFAULT_RACE_ETHINICITY_CODE);
        assertThat(testPersonDetails.getCitizenshipStatusCode()).isEqualTo(DEFAULT_CITIZENSHIP_STATUS_CODE);
        assertThat(testPersonDetails.getPregnant()).isEqualTo(DEFAULT_PREGNANT);
        assertThat(testPersonDetails.getChildrenCount()).isEqualTo(DEFAULT_CHILDREN_COUNT);
        assertThat(testPersonDetails.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPersonDetails.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void createPersonDetailsWithExistingId() throws Exception {
        // Create the PersonDetails with an existing ID
        personDetails.setId(1L);

        int databaseSizeBeforeCreate = personDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonDetails() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get all the personDetailsList
        restPersonDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].maritalTypeStatusCode").value(hasItem(DEFAULT_MARITAL_TYPE_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].raceEthinicityCode").value(hasItem(DEFAULT_RACE_ETHINICITY_CODE)))
            .andExpect(jsonPath("$.[*].citizenshipStatusCode").value(hasItem(DEFAULT_CITIZENSHIP_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].pregnant").value(hasItem(DEFAULT_PREGNANT.booleanValue())))
            .andExpect(jsonPath("$.[*].childrenCount").value(hasItem(DEFAULT_CHILDREN_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    void getPersonDetails() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        // Get the personDetails
        restPersonDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, personDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personDetails.getId().intValue()))
            .andExpect(jsonPath("$.maritalTypeStatusCode").value(DEFAULT_MARITAL_TYPE_STATUS_CODE))
            .andExpect(jsonPath("$.raceEthinicityCode").value(DEFAULT_RACE_ETHINICITY_CODE))
            .andExpect(jsonPath("$.citizenshipStatusCode").value(DEFAULT_CITIZENSHIP_STATUS_CODE))
            .andExpect(jsonPath("$.pregnant").value(DEFAULT_PREGNANT.booleanValue()))
            .andExpect(jsonPath("$.childrenCount").value(DEFAULT_CHILDREN_COUNT.intValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    void getNonExistingPersonDetails() throws Exception {
        // Get the personDetails
        restPersonDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonDetails() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();

        // Update the personDetails
        PersonDetails updatedPersonDetails = personDetailsRepository.findById(personDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPersonDetails are not directly saved in db
        em.detach(updatedPersonDetails);
        updatedPersonDetails
            .maritalTypeStatusCode(UPDATED_MARITAL_TYPE_STATUS_CODE)
            .raceEthinicityCode(UPDATED_RACE_ETHINICITY_CODE)
            .citizenshipStatusCode(UPDATED_CITIZENSHIP_STATUS_CODE)
            .pregnant(UPDATED_PREGNANT)
            .childrenCount(UPDATED_CHILDREN_COUNT)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT);

        restPersonDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonDetails))
            )
            .andExpect(status().isOk());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
        PersonDetails testPersonDetails = personDetailsList.get(personDetailsList.size() - 1);
        assertThat(testPersonDetails.getMaritalTypeStatusCode()).isEqualTo(UPDATED_MARITAL_TYPE_STATUS_CODE);
        assertThat(testPersonDetails.getRaceEthinicityCode()).isEqualTo(UPDATED_RACE_ETHINICITY_CODE);
        assertThat(testPersonDetails.getCitizenshipStatusCode()).isEqualTo(UPDATED_CITIZENSHIP_STATUS_CODE);
        assertThat(testPersonDetails.getPregnant()).isEqualTo(UPDATED_PREGNANT);
        assertThat(testPersonDetails.getChildrenCount()).isEqualTo(UPDATED_CHILDREN_COUNT);
        assertThat(testPersonDetails.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPersonDetails.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingPersonDetails() throws Exception {
        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();
        personDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonDetails() throws Exception {
        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();
        personDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonDetails() throws Exception {
        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();
        personDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonDetailsWithPatch() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();

        // Update the personDetails using partial update
        PersonDetails partialUpdatedPersonDetails = new PersonDetails();
        partialUpdatedPersonDetails.setId(personDetails.getId());

        partialUpdatedPersonDetails
            .maritalTypeStatusCode(UPDATED_MARITAL_TYPE_STATUS_CODE)
            .raceEthinicityCode(UPDATED_RACE_ETHINICITY_CODE)
            .citizenshipStatusCode(UPDATED_CITIZENSHIP_STATUS_CODE)
            .childrenCount(UPDATED_CHILDREN_COUNT);

        restPersonDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonDetails))
            )
            .andExpect(status().isOk());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
        PersonDetails testPersonDetails = personDetailsList.get(personDetailsList.size() - 1);
        assertThat(testPersonDetails.getMaritalTypeStatusCode()).isEqualTo(UPDATED_MARITAL_TYPE_STATUS_CODE);
        assertThat(testPersonDetails.getRaceEthinicityCode()).isEqualTo(UPDATED_RACE_ETHINICITY_CODE);
        assertThat(testPersonDetails.getCitizenshipStatusCode()).isEqualTo(UPDATED_CITIZENSHIP_STATUS_CODE);
        assertThat(testPersonDetails.getPregnant()).isEqualTo(DEFAULT_PREGNANT);
        assertThat(testPersonDetails.getChildrenCount()).isEqualTo(UPDATED_CHILDREN_COUNT);
        assertThat(testPersonDetails.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPersonDetails.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void fullUpdatePersonDetailsWithPatch() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();

        // Update the personDetails using partial update
        PersonDetails partialUpdatedPersonDetails = new PersonDetails();
        partialUpdatedPersonDetails.setId(personDetails.getId());

        partialUpdatedPersonDetails
            .maritalTypeStatusCode(UPDATED_MARITAL_TYPE_STATUS_CODE)
            .raceEthinicityCode(UPDATED_RACE_ETHINICITY_CODE)
            .citizenshipStatusCode(UPDATED_CITIZENSHIP_STATUS_CODE)
            .pregnant(UPDATED_PREGNANT)
            .childrenCount(UPDATED_CHILDREN_COUNT)
            .height(UPDATED_HEIGHT)
            .weight(UPDATED_WEIGHT);

        restPersonDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonDetails))
            )
            .andExpect(status().isOk());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
        PersonDetails testPersonDetails = personDetailsList.get(personDetailsList.size() - 1);
        assertThat(testPersonDetails.getMaritalTypeStatusCode()).isEqualTo(UPDATED_MARITAL_TYPE_STATUS_CODE);
        assertThat(testPersonDetails.getRaceEthinicityCode()).isEqualTo(UPDATED_RACE_ETHINICITY_CODE);
        assertThat(testPersonDetails.getCitizenshipStatusCode()).isEqualTo(UPDATED_CITIZENSHIP_STATUS_CODE);
        assertThat(testPersonDetails.getPregnant()).isEqualTo(UPDATED_PREGNANT);
        assertThat(testPersonDetails.getChildrenCount()).isEqualTo(UPDATED_CHILDREN_COUNT);
        assertThat(testPersonDetails.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPersonDetails.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingPersonDetails() throws Exception {
        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();
        personDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonDetails() throws Exception {
        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();
        personDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonDetails() throws Exception {
        int databaseSizeBeforeUpdate = personDetailsRepository.findAll().size();
        personDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonDetails in the database
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonDetails() throws Exception {
        // Initialize the database
        personDetailsRepository.saveAndFlush(personDetails);

        int databaseSizeBeforeDelete = personDetailsRepository.findAll().size();

        // Delete the personDetails
        restPersonDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, personDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonDetails> personDetailsList = personDetailsRepository.findAll();
        assertThat(personDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
