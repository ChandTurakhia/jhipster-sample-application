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
 * A PersonName.
 */
@Entity
@Table(name = "person_name")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "second_last_name")
    private String secondLastName;

    @Column(name = "preferred_name")
    private String preferredName;

    @Column(name = "prefix_code")
    private String prefixCode;

    @Column(name = "suffix_code")
    private String suffixCode;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @OneToMany(mappedBy = "personName")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personDetails", "personName", "personAddress", "personLanguage" }, allowSetters = true)
    private Set<Person> people = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonName id(Long id) {
        this.id = id;
        return this;
    }

    public Long getPersonId() {
        return this.personId;
    }

    public PersonName personId(Long personId) {
        this.personId = personId;
        return this;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public PersonName firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public PersonName middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public PersonName lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return this.secondLastName;
    }

    public PersonName secondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
        return this;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getPreferredName() {
        return this.preferredName;
    }

    public PersonName preferredName(String preferredName) {
        this.preferredName = preferredName;
        return this;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getPrefixCode() {
        return this.prefixCode;
    }

    public PersonName prefixCode(String prefixCode) {
        this.prefixCode = prefixCode;
        return this;
    }

    public void setPrefixCode(String prefixCode) {
        this.prefixCode = prefixCode;
    }

    public String getSuffixCode() {
        return this.suffixCode;
    }

    public PersonName suffixCode(String suffixCode) {
        this.suffixCode = suffixCode;
        return this;
    }

    public void setSuffixCode(String suffixCode) {
        this.suffixCode = suffixCode;
    }

    public Instant getValidFrom() {
        return this.validFrom;
    }

    public PersonName validFrom(Instant validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return this.validTo;
    }

    public PersonName validTo(Instant validTo) {
        this.validTo = validTo;
        return this;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public PersonName people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public PersonName addPerson(Person person) {
        this.people.add(person);
        person.setPersonName(this);
        return this;
    }

    public PersonName removePerson(Person person) {
        this.people.remove(person);
        person.setPersonName(null);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setPersonName(null));
        }
        if (people != null) {
            people.forEach(i -> i.setPersonName(this));
        }
        this.people = people;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonName)) {
            return false;
        }
        return id != null && id.equals(((PersonName) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonName{" +
            "id=" + getId() +
            ", personId=" + getPersonId() +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", secondLastName='" + getSecondLastName() + "'" +
            ", preferredName='" + getPreferredName() + "'" +
            ", prefixCode='" + getPrefixCode() + "'" +
            ", suffixCode='" + getSuffixCode() + "'" +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            "}";
    }
}
