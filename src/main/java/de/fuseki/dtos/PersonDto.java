package de.fuseki.dtos;

import de.fuseki.converter.AddressConverter;
import de.fuseki.converter.LocalDateConvert;
import de.fuseki.entities.Address;
import de.fuseki.enums.PersonType;
import jakarta.persistence.Convert;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDto {
    private Integer id;
    private String name;
    private String surName;
    @Enumerated(EnumType.STRING)
    private PersonType personType;
    private String email;
    @Convert(converter = AddressConverter.class)
    private Address address;
    @Convert(converter = LocalDateConvert.class)
    private LocalDate birthDate;
}
