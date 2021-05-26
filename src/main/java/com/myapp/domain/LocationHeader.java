package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LocationHeader.
 */
@Entity
@Table(name = "location_header")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocationHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private Long latitude;

    @Column(name = "longitude")
    private Long longitude;

    @Column(name = "elevation")
    private Long elevation;

    @JsonIgnoreProperties(value = { "personAddress", "locationHeader" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AddressHeader addressHeader;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationHeader id(Long id) {
        this.id = id;
        return this;
    }

    public Long getLatitude() {
        return this.latitude;
    }

    public LocationHeader latitude(Long latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return this.longitude;
    }

    public LocationHeader longitude(Long longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getElevation() {
        return this.elevation;
    }

    public LocationHeader elevation(Long elevation) {
        this.elevation = elevation;
        return this;
    }

    public void setElevation(Long elevation) {
        this.elevation = elevation;
    }

    public AddressHeader getAddressHeader() {
        return this.addressHeader;
    }

    public LocationHeader addressHeader(AddressHeader addressHeader) {
        this.setAddressHeader(addressHeader);
        return this;
    }

    public void setAddressHeader(AddressHeader addressHeader) {
        this.addressHeader = addressHeader;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationHeader)) {
            return false;
        }
        return id != null && id.equals(((LocationHeader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationHeader{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", elevation=" + getElevation() +
            "}";
    }
}
