package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonAddress.
 */
@Entity
@Table(name = "person_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_type_code")
    private String addressTypeCode;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @OneToMany(mappedBy = "personAddress")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personDetails", "personName", "personAddress", "personLanguage" }, allowSetters = true)
    private Set<Person> people = new HashSet<>();

    @JsonIgnoreProperties(value = { "personAddress", "locationHeader" }, allowSetters = true)
    @OneToOne(mappedBy = "personAddress")
    private AddressHeader addressHeader;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonAddress id(Long id) {
        this.id = id;
        return this;
    }

    public String getAddressTypeCode() {
        return this.addressTypeCode;
    }

    public PersonAddress addressTypeCode(String addressTypeCode) {
        this.addressTypeCode = addressTypeCode;
        return this;
    }

    public void setAddressTypeCode(String addressTypeCode) {
        this.addressTypeCode = addressTypeCode;
    }

    public Instant getValidFrom() {
        return this.validFrom;
    }

    public PersonAddress validFrom(Instant validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return this.validTo;
    }

    public PersonAddress validTo(Instant validTo) {
        this.validTo = validTo;
        return this;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public PersonAddress people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public PersonAddress addPerson(Person person) {
        this.people.add(person);
        person.setPersonAddress(this);
        return this;
    }

    public PersonAddress removePerson(Person person) {
        this.people.remove(person);
        person.setPersonAddress(null);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setPersonAddress(null));
        }
        if (people != null) {
            people.forEach(i -> i.setPersonAddress(this));
        }
        this.people = people;
    }

    public AddressHeader getAddressHeader() {
        return this.addressHeader;
    }

    public PersonAddress addressHeader(AddressHeader addressHeader) {
        this.setAddressHeader(addressHeader);
        return this;
    }

    public void setAddressHeader(AddressHeader addressHeader) {
        if (this.addressHeader != null) {
            this.addressHeader.setPersonAddress(null);
        }
        if (addressHeader != null) {
            addressHeader.setPersonAddress(this);
        }
        this.addressHeader = addressHeader;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonAddress)) {
            return false;
        }
        return id != null && id.equals(((PersonAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonAddress{" +
            "id=" + getId() +
            ", addressTypeCode='" + getAddressTypeCode() + "'" +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            "}";
    }
}
