package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private Long version;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "date_of_death")
    private Instant dateOfDeath;

    @Column(name = "gender_type_code")
    private String genderTypeCode;

    @JsonIgnoreProperties(value = { "person" }, allowSetters = true)
    @OneToOne(mappedBy = "person")
    private PersonDetails personDetails;

    @ManyToOne
    @JsonIgnoreProperties(value = { "people" }, allowSetters = true)
    private PersonName personName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "people", "addressHeader" }, allowSetters = true)
    private PersonAddress personAddress;

    @ManyToOne
    @JsonIgnoreProperties(value = { "people" }, allowSetters = true)
    private PersonLanguage personLanguage;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person id(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return this.version;
    }

    public Person version(Long version) {
        this.version = version;
        return this;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public Person stateCode(String stateCode) {
        this.stateCode = stateCode;
        return this;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Instant getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Person dateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Instant getDateOfDeath() {
        return this.dateOfDeath;
    }

    public Person dateOfDeath(Instant dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
        return this;
    }

    public void setDateOfDeath(Instant dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getGenderTypeCode() {
        return this.genderTypeCode;
    }

    public Person genderTypeCode(String genderTypeCode) {
        this.genderTypeCode = genderTypeCode;
        return this;
    }

    public void setGenderTypeCode(String genderTypeCode) {
        this.genderTypeCode = genderTypeCode;
    }

    public PersonDetails getPersonDetails() {
        return this.personDetails;
    }

    public Person personDetails(PersonDetails personDetails) {
        this.setPersonDetails(personDetails);
        return this;
    }

    public void setPersonDetails(PersonDetails personDetails) {
        if (this.personDetails != null) {
            this.personDetails.setPerson(null);
        }
        if (personDetails != null) {
            personDetails.setPerson(this);
        }
        this.personDetails = personDetails;
    }

    public PersonName getPersonName() {
        return this.personName;
    }

    public Person personName(PersonName personName) {
        this.setPersonName(personName);
        return this;
    }

    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }

    public PersonAddress getPersonAddress() {
        return this.personAddress;
    }

    public Person personAddress(PersonAddress personAddress) {
        this.setPersonAddress(personAddress);
        return this;
    }

    public void setPersonAddress(PersonAddress personAddress) {
        this.personAddress = personAddress;
    }

    public PersonLanguage getPersonLanguage() {
        return this.personLanguage;
    }

    public Person personLanguage(PersonLanguage personLanguage) {
        this.setPersonLanguage(personLanguage);
        return this;
    }

    public void setPersonLanguage(PersonLanguage personLanguage) {
        this.personLanguage = personLanguage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", version=" + getVersion() +
            ", stateCode='" + getStateCode() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", dateOfDeath='" + getDateOfDeath() + "'" +
            ", genderTypeCode='" + getGenderTypeCode() + "'" +
            "}";
    }
}
