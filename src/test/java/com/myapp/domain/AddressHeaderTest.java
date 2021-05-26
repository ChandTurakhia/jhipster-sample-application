package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressHeaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressHeader.class);
        AddressHeader addressHeader1 = new AddressHeader();
        addressHeader1.setId(1L);
        AddressHeader addressHeader2 = new AddressHeader();
        addressHeader2.setId(addressHeader1.getId());
        assertThat(addressHeader1).isEqualTo(addressHeader2);
        addressHeader2.setId(2L);
        assertThat(addressHeader1).isNotEqualTo(addressHeader2);
        addressHeader1.setId(null);
        assertThat(addressHeader1).isNotEqualTo(addressHeader2);
    }
}
