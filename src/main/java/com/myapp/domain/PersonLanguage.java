package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonLanguage.
 */
@Entity
@Table(name = "person_language")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "language_usage_code")
    private String languageUsageCode;

    @Column(name = "preferred_language")
    private Boolean preferredLanguage;

    @OneToMany(mappedBy = "personLanguage")
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

    public PersonLanguage id(Long id) {
        this.id = id;
        return this;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public PersonLanguage languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageUsageCode() {
        return this.languageUsageCode;
    }

    public PersonLanguage languageUsageCode(String languageUsageCode) {
        this.languageUsageCode = languageUsageCode;
        return this;
    }

    public void setLanguageUsageCode(String languageUsageCode) {
        this.languageUsageCode = languageUsageCode;
    }

    public Boolean getPreferredLanguage() {
        return this.preferredLanguage;
    }

    public PersonLanguage preferredLanguage(Boolean preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        return this;
    }

    public void setPreferredLanguage(Boolean preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public PersonLanguage people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public PersonLanguage addPerson(Person person) {
        this.people.add(person);
        person.setPersonLanguage(this);
        return this;
    }

    public PersonLanguage removePerson(Person person) {
        this.people.remove(person);
        person.setPersonLanguage(null);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setPersonLanguage(null));
        }
        if (people != null) {
            people.forEach(i -> i.setPersonLanguage(this));
        }
        this.people = people;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonLanguage)) {
            return false;
        }
        return id != null && id.equals(((PersonLanguage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonLanguage{" +
            "id=" + getId() +
            ", languageCode='" + getLanguageCode() + "'" +
            ", languageUsageCode='" + getLanguageUsageCode() + "'" +
            ", preferredLanguage='" + getPreferredLanguage() + "'" +
            "}";
    }
}
