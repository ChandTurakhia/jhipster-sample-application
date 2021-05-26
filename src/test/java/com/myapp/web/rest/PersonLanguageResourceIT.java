package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PersonLanguage;
import com.myapp.repository.PersonLanguageRepository;
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
 * Integration tests for the {@link PersonLanguageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonLanguageResourceIT {

    private static final String DEFAULT_LANGUAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE_USAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_USAGE_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PREFERRED_LANGUAGE = false;
    private static final Boolean UPDATED_PREFERRED_LANGUAGE = true;

    private static final String ENTITY_API_URL = "/api/person-languages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonLanguageRepository personLanguageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonLanguageMockMvc;

    private PersonLanguage personLanguage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonLanguage createEntity(EntityManager em) {
        PersonLanguage personLanguage = new PersonLanguage()
            .languageCode(DEFAULT_LANGUAGE_CODE)
            .languageUsageCode(DEFAULT_LANGUAGE_USAGE_CODE)
            .preferredLanguage(DEFAULT_PREFERRED_LANGUAGE);
        return personLanguage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonLanguage createUpdatedEntity(EntityManager em) {
        PersonLanguage personLanguage = new PersonLanguage()
            .languageCode(UPDATED_LANGUAGE_CODE)
            .languageUsageCode(UPDATED_LANGUAGE_USAGE_CODE)
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE);
        return personLanguage;
    }

    @BeforeEach
    public void initTest() {
        personLanguage = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonLanguage() throws Exception {
        int databaseSizeBeforeCreate = personLanguageRepository.findAll().size();
        // Create the PersonLanguage
        restPersonLanguageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personLanguage))
            )
            .andExpect(status().isCreated());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeCreate + 1);
        PersonLanguage testPersonLanguage = personLanguageList.get(personLanguageList.size() - 1);
        assertThat(testPersonLanguage.getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(testPersonLanguage.getLanguageUsageCode()).isEqualTo(DEFAULT_LANGUAGE_USAGE_CODE);
        assertThat(testPersonLanguage.getPreferredLanguage()).isEqualTo(DEFAULT_PREFERRED_LANGUAGE);
    }

    @Test
    @Transactional
    void createPersonLanguageWithExistingId() throws Exception {
        // Create the PersonLanguage with an existing ID
        personLanguage.setId(1L);

        int databaseSizeBeforeCreate = personLanguageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonLanguageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personLanguage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonLanguages() throws Exception {
        // Initialize the database
        personLanguageRepository.saveAndFlush(personLanguage);

        // Get all the personLanguageList
        restPersonLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personLanguage.getId().intValue())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE)))
            .andExpect(jsonPath("$.[*].languageUsageCode").value(hasItem(DEFAULT_LANGUAGE_USAGE_CODE)))
            .andExpect(jsonPath("$.[*].preferredLanguage").value(hasItem(DEFAULT_PREFERRED_LANGUAGE.booleanValue())));
    }

    @Test
    @Transactional
    void getPersonLanguage() throws Exception {
        // Initialize the database
        personLanguageRepository.saveAndFlush(personLanguage);

        // Get the personLanguage
        restPersonLanguageMockMvc
            .perform(get(ENTITY_API_URL_ID, personLanguage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personLanguage.getId().intValue()))
            .andExpect(jsonPath("$.languageCode").value(DEFAULT_LANGUAGE_CODE))
            .andExpect(jsonPath("$.languageUsageCode").value(DEFAULT_LANGUAGE_USAGE_CODE))
            .andExpect(jsonPath("$.preferredLanguage").value(DEFAULT_PREFERRED_LANGUAGE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPersonLanguage() throws Exception {
        // Get the personLanguage
        restPersonLanguageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonLanguage() throws Exception {
        // Initialize the database
        personLanguageRepository.saveAndFlush(personLanguage);

        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();

        // Update the personLanguage
        PersonLanguage updatedPersonLanguage = personLanguageRepository.findById(personLanguage.getId()).get();
        // Disconnect from session so that the updates on updatedPersonLanguage are not directly saved in db
        em.detach(updatedPersonLanguage);
        updatedPersonLanguage
            .languageCode(UPDATED_LANGUAGE_CODE)
            .languageUsageCode(UPDATED_LANGUAGE_USAGE_CODE)
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE);

        restPersonLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonLanguage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonLanguage))
            )
            .andExpect(status().isOk());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
        PersonLanguage testPersonLanguage = personLanguageList.get(personLanguageList.size() - 1);
        assertThat(testPersonLanguage.getLanguageCode()).isEqualTo(UPDATED_LANGUAGE_CODE);
        assertThat(testPersonLanguage.getLanguageUsageCode()).isEqualTo(UPDATED_LANGUAGE_USAGE_CODE);
        assertThat(testPersonLanguage.getPreferredLanguage()).isEqualTo(UPDATED_PREFERRED_LANGUAGE);
    }

    @Test
    @Transactional
    void putNonExistingPersonLanguage() throws Exception {
        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();
        personLanguage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personLanguage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personLanguage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonLanguage() throws Exception {
        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();
        personLanguage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personLanguage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonLanguage() throws Exception {
        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();
        personLanguage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonLanguageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personLanguage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonLanguageWithPatch() throws Exception {
        // Initialize the database
        personLanguageRepository.saveAndFlush(personLanguage);

        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();

        // Update the personLanguage using partial update
        PersonLanguage partialUpdatedPersonLanguage = new PersonLanguage();
        partialUpdatedPersonLanguage.setId(personLanguage.getId());

        restPersonLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonLanguage))
            )
            .andExpect(status().isOk());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
        PersonLanguage testPersonLanguage = personLanguageList.get(personLanguageList.size() - 1);
        assertThat(testPersonLanguage.getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(testPersonLanguage.getLanguageUsageCode()).isEqualTo(DEFAULT_LANGUAGE_USAGE_CODE);
        assertThat(testPersonLanguage.getPreferredLanguage()).isEqualTo(DEFAULT_PREFERRED_LANGUAGE);
    }

    @Test
    @Transactional
    void fullUpdatePersonLanguageWithPatch() throws Exception {
        // Initialize the database
        personLanguageRepository.saveAndFlush(personLanguage);

        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();

        // Update the personLanguage using partial update
        PersonLanguage partialUpdatedPersonLanguage = new PersonLanguage();
        partialUpdatedPersonLanguage.setId(personLanguage.getId());

        partialUpdatedPersonLanguage
            .languageCode(UPDATED_LANGUAGE_CODE)
            .languageUsageCode(UPDATED_LANGUAGE_USAGE_CODE)
            .preferredLanguage(UPDATED_PREFERRED_LANGUAGE);

        restPersonLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonLanguage))
            )
            .andExpect(status().isOk());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
        PersonLanguage testPersonLanguage = personLanguageList.get(personLanguageList.size() - 1);
        assertThat(testPersonLanguage.getLanguageCode()).isEqualTo(UPDATED_LANGUAGE_CODE);
        assertThat(testPersonLanguage.getLanguageUsageCode()).isEqualTo(UPDATED_LANGUAGE_USAGE_CODE);
        assertThat(testPersonLanguage.getPreferredLanguage()).isEqualTo(UPDATED_PREFERRED_LANGUAGE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonLanguage() throws Exception {
        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();
        personLanguage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personLanguage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personLanguage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonLanguage() throws Exception {
        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();
        personLanguage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personLanguage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonLanguage() throws Exception {
        int databaseSizeBeforeUpdate = personLanguageRepository.findAll().size();
        personLanguage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonLanguageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personLanguage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonLanguage in the database
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonLanguage() throws Exception {
        // Initialize the database
        personLanguageRepository.saveAndFlush(personLanguage);

        int databaseSizeBeforeDelete = personLanguageRepository.findAll().size();

        // Delete the personLanguage
        restPersonLanguageMockMvc
            .perform(delete(ENTITY_API_URL_ID, personLanguage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonLanguage> personLanguageList = personLanguageRepository.findAll();
        assertThat(personLanguageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
