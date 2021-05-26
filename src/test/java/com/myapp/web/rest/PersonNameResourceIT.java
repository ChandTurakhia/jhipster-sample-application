package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PersonName;
import com.myapp.repository.PersonNameRepository;
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
 * Integration tests for the {@link PersonNameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonNameResourceIT {

    private static final Long DEFAULT_PERSON_ID = 1L;
    private static final Long UPDATED_PERSON_ID = 2L;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERRED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUFFIX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUFFIX_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_VALID_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/person-names";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonNameRepository personNameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonNameMockMvc;

    private PersonName personName;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonName createEntity(EntityManager em) {
        PersonName personName = new PersonName()
            .personId(DEFAULT_PERSON_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .secondLastName(DEFAULT_SECOND_LAST_NAME)
            .preferredName(DEFAULT_PREFERRED_NAME)
            .prefixCode(DEFAULT_PREFIX_CODE)
            .suffixCode(DEFAULT_SUFFIX_CODE)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO);
        return personName;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonName createUpdatedEntity(EntityManager em) {
        PersonName personName = new PersonName()
            .personId(UPDATED_PERSON_ID)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .preferredName(UPDATED_PREFERRED_NAME)
            .prefixCode(UPDATED_PREFIX_CODE)
            .suffixCode(UPDATED_SUFFIX_CODE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);
        return personName;
    }

    @BeforeEach
    public void initTest() {
        personName = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonName() throws Exception {
        int databaseSizeBeforeCreate = personNameRepository.findAll().size();
        // Create the PersonName
        restPersonNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personName)))
            .andExpect(status().isCreated());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeCreate + 1);
        PersonName testPersonName = personNameList.get(personNameList.size() - 1);
        assertThat(testPersonName.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testPersonName.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPersonName.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testPersonName.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPersonName.getSecondLastName()).isEqualTo(DEFAULT_SECOND_LAST_NAME);
        assertThat(testPersonName.getPreferredName()).isEqualTo(DEFAULT_PREFERRED_NAME);
        assertThat(testPersonName.getPrefixCode()).isEqualTo(DEFAULT_PREFIX_CODE);
        assertThat(testPersonName.getSuffixCode()).isEqualTo(DEFAULT_SUFFIX_CODE);
        assertThat(testPersonName.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testPersonName.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
    }

    @Test
    @Transactional
    void createPersonNameWithExistingId() throws Exception {
        // Create the PersonName with an existing ID
        personName.setId(1L);

        int databaseSizeBeforeCreate = personNameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personName)))
            .andExpect(status().isBadRequest());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonNames() throws Exception {
        // Initialize the database
        personNameRepository.saveAndFlush(personName);

        // Get all the personNameList
        restPersonNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personName.getId().intValue())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].secondLastName").value(hasItem(DEFAULT_SECOND_LAST_NAME)))
            .andExpect(jsonPath("$.[*].preferredName").value(hasItem(DEFAULT_PREFERRED_NAME)))
            .andExpect(jsonPath("$.[*].prefixCode").value(hasItem(DEFAULT_PREFIX_CODE)))
            .andExpect(jsonPath("$.[*].suffixCode").value(hasItem(DEFAULT_SUFFIX_CODE)))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())));
    }

    @Test
    @Transactional
    void getPersonName() throws Exception {
        // Initialize the database
        personNameRepository.saveAndFlush(personName);

        // Get the personName
        restPersonNameMockMvc
            .perform(get(ENTITY_API_URL_ID, personName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personName.getId().intValue()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.secondLastName").value(DEFAULT_SECOND_LAST_NAME))
            .andExpect(jsonPath("$.preferredName").value(DEFAULT_PREFERRED_NAME))
            .andExpect(jsonPath("$.prefixCode").value(DEFAULT_PREFIX_CODE))
            .andExpect(jsonPath("$.suffixCode").value(DEFAULT_SUFFIX_CODE))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonName() throws Exception {
        // Get the personName
        restPersonNameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonName() throws Exception {
        // Initialize the database
        personNameRepository.saveAndFlush(personName);

        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();

        // Update the personName
        PersonName updatedPersonName = personNameRepository.findById(personName.getId()).get();
        // Disconnect from session so that the updates on updatedPersonName are not directly saved in db
        em.detach(updatedPersonName);
        updatedPersonName
            .personId(UPDATED_PERSON_ID)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .preferredName(UPDATED_PREFERRED_NAME)
            .prefixCode(UPDATED_PREFIX_CODE)
            .suffixCode(UPDATED_SUFFIX_CODE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restPersonNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonName.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonName))
            )
            .andExpect(status().isOk());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
        PersonName testPersonName = personNameList.get(personNameList.size() - 1);
        assertThat(testPersonName.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testPersonName.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPersonName.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testPersonName.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPersonName.getSecondLastName()).isEqualTo(UPDATED_SECOND_LAST_NAME);
        assertThat(testPersonName.getPreferredName()).isEqualTo(UPDATED_PREFERRED_NAME);
        assertThat(testPersonName.getPrefixCode()).isEqualTo(UPDATED_PREFIX_CODE);
        assertThat(testPersonName.getSuffixCode()).isEqualTo(UPDATED_SUFFIX_CODE);
        assertThat(testPersonName.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testPersonName.getValidTo()).isEqualTo(UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void putNonExistingPersonName() throws Exception {
        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();
        personName.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personName.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personName))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonName() throws Exception {
        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();
        personName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personName))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonName() throws Exception {
        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();
        personName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personName)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonNameWithPatch() throws Exception {
        // Initialize the database
        personNameRepository.saveAndFlush(personName);

        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();

        // Update the personName using partial update
        PersonName partialUpdatedPersonName = new PersonName();
        partialUpdatedPersonName.setId(personName.getId());

        partialUpdatedPersonName
            .lastName(UPDATED_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .prefixCode(UPDATED_PREFIX_CODE)
            .suffixCode(UPDATED_SUFFIX_CODE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restPersonNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonName))
            )
            .andExpect(status().isOk());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
        PersonName testPersonName = personNameList.get(personNameList.size() - 1);
        assertThat(testPersonName.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testPersonName.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPersonName.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testPersonName.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPersonName.getSecondLastName()).isEqualTo(UPDATED_SECOND_LAST_NAME);
        assertThat(testPersonName.getPreferredName()).isEqualTo(DEFAULT_PREFERRED_NAME);
        assertThat(testPersonName.getPrefixCode()).isEqualTo(UPDATED_PREFIX_CODE);
        assertThat(testPersonName.getSuffixCode()).isEqualTo(UPDATED_SUFFIX_CODE);
        assertThat(testPersonName.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testPersonName.getValidTo()).isEqualTo(UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void fullUpdatePersonNameWithPatch() throws Exception {
        // Initialize the database
        personNameRepository.saveAndFlush(personName);

        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();

        // Update the personName using partial update
        PersonName partialUpdatedPersonName = new PersonName();
        partialUpdatedPersonName.setId(personName.getId());

        partialUpdatedPersonName
            .personId(UPDATED_PERSON_ID)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .preferredName(UPDATED_PREFERRED_NAME)
            .prefixCode(UPDATED_PREFIX_CODE)
            .suffixCode(UPDATED_SUFFIX_CODE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restPersonNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonName))
            )
            .andExpect(status().isOk());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
        PersonName testPersonName = personNameList.get(personNameList.size() - 1);
        assertThat(testPersonName.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testPersonName.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPersonName.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testPersonName.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPersonName.getSecondLastName()).isEqualTo(UPDATED_SECOND_LAST_NAME);
        assertThat(testPersonName.getPreferredName()).isEqualTo(UPDATED_PREFERRED_NAME);
        assertThat(testPersonName.getPrefixCode()).isEqualTo(UPDATED_PREFIX_CODE);
        assertThat(testPersonName.getSuffixCode()).isEqualTo(UPDATED_SUFFIX_CODE);
        assertThat(testPersonName.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testPersonName.getValidTo()).isEqualTo(UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void patchNonExistingPersonName() throws Exception {
        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();
        personName.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personName))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonName() throws Exception {
        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();
        personName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personName))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonName() throws Exception {
        int databaseSizeBeforeUpdate = personNameRepository.findAll().size();
        personName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNameMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personName))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonName in the database
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonName() throws Exception {
        // Initialize the database
        personNameRepository.saveAndFlush(personName);

        int databaseSizeBeforeDelete = personNameRepository.findAll().size();

        // Delete the personName
        restPersonNameMockMvc
            .perform(delete(ENTITY_API_URL_ID, personName.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonName> personNameList = personNameRepository.findAll();
        assertThat(personNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
