package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonLanguageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonLanguage.class);
        PersonLanguage personLanguage1 = new PersonLanguage();
        personLanguage1.setId(1L);
        PersonLanguage personLanguage2 = new PersonLanguage();
        personLanguage2.setId(personLanguage1.getId());
        assertThat(personLanguage1).isEqualTo(personLanguage2);
        personLanguage2.setId(2L);
        assertThat(personLanguage1).isNotEqualTo(personLanguage2);
        personLanguage1.setId(null);
        assertThat(personLanguage1).isNotEqualTo(personLanguage2);
    }
}
