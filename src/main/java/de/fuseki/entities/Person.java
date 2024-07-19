package de.fuseki.entities;

import de.fuseki.converter.AddressConverter;
import de.fuseki.converter.LocalDateConvert;
import de.fuseki.enums.PersonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Builder
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surName;

    @Column(name = "person_type", nullable = false)
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
    private List<MediaOrder> mediaOrderList;
}
