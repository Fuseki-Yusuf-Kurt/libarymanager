package de.fuseki.entities;


import de.fuseki.converter.AddressConverter;
import de.fuseki.converter.LocalDateConvert;
import de.fuseki.enums.PersonType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

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

    @Convert(converter = LocalDateConvert.class)
    @Column(name = "birth_date", nullable = false, columnDefinition = "TEXT")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "person")
    private List<Order> orderList;

    public Person(int id, String name, String surName, PersonType personType, String email, Address address, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.personType = personType;
        this.email = email;
        this.address = address;
        this.birthDate = birthDate;
    }
}
