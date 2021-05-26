package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AddressHeader.
 */
@Entity
@Table(name = "address_header")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AddressHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "standardized")
    private Boolean standardized;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "address_line_3")
    private String addressLine3;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "county_name")
    private String countyName;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country_name")
    private String countryName;

    @JsonIgnoreProperties(value = { "people", "addressHeader" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PersonAddress personAddress;

    @JsonIgnoreProperties(value = { "addressHeader" }, allowSetters = true)
    @OneToOne(mappedBy = "addressHeader")
    private LocationHeader locationHeader;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressHeader id(Long id) {
        this.id = id;
        return this;
    }

    public String getTypeCode() {
        return this.typeCode;
    }

    public AddressHeader typeCode(String typeCode) {
        this.typeCode = typeCode;
        return this;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Boolean getStandardized() {
        return this.standardized;
    }

    public AddressHeader standardized(Boolean standardized) {
        this.standardized = standardized;
        return this;
    }

    public void setStandardized(Boolean standardized) {
        this.standardized = standardized;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public AddressHeader addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public AddressHeader addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return this.addressLine3;
    }

    public AddressHeader addressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
        return this;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCityName() {
        return this.cityName;
    }

    public AddressHeader cityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return this.countyName;
    }

    public AddressHeader countyName(String countyName) {
        this.countyName = countyName;
        return this;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public AddressHeader stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public AddressHeader zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public AddressHeader countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public PersonAddress getPersonAddress() {
        return this.personAddress;
    }

    public AddressHeader personAddress(PersonAddress personAddress) {
        this.setPersonAddress(personAddress);
        return this;
    }

    public void setPersonAddress(PersonAddress personAddress) {
        this.personAddress = personAddress;
    }

    public LocationHeader getLocationHeader() {
        return this.locationHeader;
    }

    public AddressHeader locationHeader(LocationHeader locationHeader) {
        this.setLocationHeader(locationHeader);
        return this;
    }

    public void setLocationHeader(LocationHeader locationHeader) {
        if (this.locationHeader != null) {
            this.locationHeader.setAddressHeader(null);
        }
        if (locationHeader != null) {
            locationHeader.setAddressHeader(this);
        }
        this.locationHeader = locationHeader;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressHeader)) {
            return false;
        }
        return id != null && id.equals(((AddressHeader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressHeader{" +
            "id=" + getId() +
            ", typeCode='" + getTypeCode() + "'" +
            ", standardized='" + getStandardized() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", addressLine3='" + getAddressLine3() + "'" +
            ", cityName='" + getCityName() + "'" +
            ", countyName='" + getCountyName() + "'" +
            ", stateCode='" + getStateCode() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", countryName='" + getCountryName() + "'" +
            "}";
    }
}
