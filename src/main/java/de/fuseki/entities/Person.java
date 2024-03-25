package de.fuseki.entities;


import de.fuseki.converter.AddressConverter;
import de.fuseki.converter.BirthDateConvert;
import de.fuseki.enums.PersonType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surName;

    @Column(name = "person_Type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonType personType;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Convert(converter = AddressConverter.class)
    @Column(name = "address", nullable = false)
    private Address address;

    @Convert(converter = BirthDateConvert.class)
    @Column(name = "birth_date", nullable = false, columnDefinition = "TEXT")
    private LocalDate birthDate;

}
