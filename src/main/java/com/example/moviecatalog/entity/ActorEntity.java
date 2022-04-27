package com.example.moviecatalog.entity;

import com.example.moviecatalog.enums.Gender;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "actor_entity")
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false, length = 300)
    private String name;
    @Column(nullable = false, length = 300)
    private String surname;
    @Column(length = 300)
    private String middleName;
    @Enumerated
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false)
    private Instant birthDate;
    @Column(nullable = false)
    private Instant activeStartDate;
    @Column(nullable = false, length = 500)
    private String birthPlace;

    public ActorEntity() {
    }

    public ActorEntity(Long id, String name, String surname, String middleName, Gender gender, Instant birthDate, Instant activeStartDate, String birthPlace) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.activeStartDate = activeStartDate;
        this.birthPlace = birthPlace;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public void setActiveStartDate(Instant activeStartDate) {
        this.activeStartDate = activeStartDate;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public Instant getActiveStartDate() {
        return activeStartDate;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}