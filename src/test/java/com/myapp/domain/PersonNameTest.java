package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonNameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonName.class);
        PersonName personName1 = new PersonName();
        personName1.setId(1L);
        PersonName personName2 = new PersonName();
        personName2.setId(personName1.getId());
        assertThat(personName1).isEqualTo(personName2);
        personName2.setId(2L);
        assertThat(personName1).isNotEqualTo(personName2);
        personName1.setId(null);
        assertThat(personName1).isNotEqualTo(personName2);
    }
}
