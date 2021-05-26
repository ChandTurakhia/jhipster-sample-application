package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonDetails.
 */
@Entity
@Table(name = "person_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marital_type_status_code")
    private String maritalTypeStatusCode;

    @Column(name = "race_ethinicity_code")
    private String raceEthinicityCode;

    @Column(name = "citizenship_status_code")
    private String citizenshipStatusCode;

    @Column(name = "pregnant")
    private Boolean pregnant;

    @Column(name = "children_count")
    private Long childrenCount;

    @Column(name = "height")
    private String height;

    @Column(name = "weight")
    private Integer weight;

    @JsonIgnoreProperties(value = { "personDetails", "personName", "personAddress", "personLanguage" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getMaritalTypeStatusCode() {
        return this.maritalTypeStatusCode;
    }

    public PersonDetails maritalTypeStatusCode(String maritalTypeStatusCode) {
        this.maritalTypeStatusCode = maritalTypeStatusCode;
        return this;
    }

    public void setMaritalTypeStatusCode(String maritalTypeStatusCode) {
        this.maritalTypeStatusCode = maritalTypeStatusCode;
    }

    public String getRaceEthinicityCode() {
        return this.raceEthinicityCode;
    }

    public PersonDetails raceEthinicityCode(String raceEthinicityCode) {
        this.raceEthinicityCode = raceEthinicityCode;
        return this;
    }

    public void setRaceEthinicityCode(String raceEthinicityCode) {
        this.raceEthinicityCode = raceEthinicityCode;
    }

    public String getCitizenshipStatusCode() {
        return this.citizenshipStatusCode;
    }

    public PersonDetails citizenshipStatusCode(String citizenshipStatusCode) {
        this.citizenshipStatusCode = citizenshipStatusCode;
        return this;
    }

    public void setCitizenshipStatusCode(String citizenshipStatusCode) {
        this.citizenshipStatusCode = citizenshipStatusCode;
    }

    public Boolean getPregnant() {
        return this.pregnant;
    }

    public PersonDetails pregnant(Boolean pregnant) {
        this.pregnant = pregnant;
        return this;
    }

    public void setPregnant(Boolean pregnant) {
        this.pregnant = pregnant;
    }

    public Long getChildrenCount() {
        return this.childrenCount;
    }

    public PersonDetails childrenCount(Long childrenCount) {
        this.childrenCount = childrenCount;
        return this;
    }

    public void setChildrenCount(Long childrenCount) {
        this.childrenCount = childrenCount;
    }

    public String getHeight() {
        return this.height;
    }

    public PersonDetails height(String height) {
        this.height = height;
        return this;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public PersonDetails weight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Person getPerson() {
        return this.person;
    }

    public PersonDetails person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDetails)) {
            return false;
        }
        return id != null && id.equals(((PersonDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonDetails{" +
            "id=" + getId() +
            ", maritalTypeStatusCode='" + getMaritalTypeStatusCode() + "'" +
            ", raceEthinicityCode='" + getRaceEthinicityCode() + "'" +
            ", citizenshipStatusCode='" + getCitizenshipStatusCode() + "'" +
            ", pregnant='" + getPregnant() + "'" +
            ", childrenCount=" + getChildrenCount() +
            ", height='" + getHeight() + "'" +
            ", weight=" + getWeight() +
            "}";
    }
}
