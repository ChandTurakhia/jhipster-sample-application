package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonAddress.class);
        PersonAddress personAddress1 = new PersonAddress();
        personAddress1.setId(1L);
        PersonAddress personAddress2 = new PersonAddress();
        personAddress2.setId(personAddress1.getId());
        assertThat(personAddress1).isEqualTo(personAddress2);
        personAddress2.setId(2L);
        assertThat(personAddress1).isNotEqualTo(personAddress2);
        personAddress1.setId(null);
        assertThat(personAddress1).isNotEqualTo(personAddress2);
    }
}
