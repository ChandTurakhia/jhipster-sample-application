package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationHeaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationHeader.class);
        LocationHeader locationHeader1 = new LocationHeader();
        locationHeader1.setId(1L);
        LocationHeader locationHeader2 = new LocationHeader();
        locationHeader2.setId(locationHeader1.getId());
        assertThat(locationHeader1).isEqualTo(locationHeader2);
        locationHeader2.setId(2L);
        assertThat(locationHeader1).isNotEqualTo(locationHeader2);
        locationHeader1.setId(null);
        assertThat(locationHeader1).isNotEqualTo(locationHeader2);
    }
}
