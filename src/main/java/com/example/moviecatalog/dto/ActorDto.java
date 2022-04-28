package com.example.moviecatalog.dto;

import com.example.moviecatalog.enums.Gender;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ActorDto implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private Gender gender;
    private Instant birthDate;
    private Instant activeStartDate;
    private String birthPlace;
    private String description;

    public ActorDto() {
    }

    public ActorDto(Long id, String name, String surname, String middleName, Gender gender, Instant birthDate, Instant activeStartDate, String birthPlace, String description) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.activeStartDate = activeStartDate;
        this.birthPlace = birthPlace;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Instant getActiveStartDate() {
        return activeStartDate;
    }

    public void setActiveStartDate(Instant activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorDto entity = (ActorDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.surname, entity.surname) &&
                Objects.equals(this.middleName, entity.middleName) &&
                Objects.equals(this.gender, entity.gender) &&
                Objects.equals(this.birthDate, entity.birthDate) &&
                Objects.equals(this.activeStartDate, entity.activeStartDate) &&
                Objects.equals(this.birthPlace, entity.birthPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, middleName, gender, birthDate, activeStartDate, birthPlace);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "surname = " + surname + ", " +
                "middleName = " + middleName + ", " +
                "gender = " + gender + ", " +
                "birthDate = " + birthDate + ", " +
                "activeStartDate = " + activeStartDate + ", " +
                "birthPlace = " + birthPlace + ")";
    }
}
