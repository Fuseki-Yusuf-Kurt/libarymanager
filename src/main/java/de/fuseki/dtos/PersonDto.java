package de.fuseki.dtos;

import de.fuseki.entities.Address;
import de.fuseki.enums.PersonType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    @NotNull
    private Integer id;
    private String name;
    private String surName;
    @Enumerated(EnumType.STRING)
    private PersonType personType;
    private String email;
    private Address address;
    private LocalDate birthDate;
}
