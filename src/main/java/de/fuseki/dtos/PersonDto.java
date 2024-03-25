package de.fuseki.dtos;

import de.fuseki.entities.Address;
import de.fuseki.enums.PersonType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PersonDto {
    @NotNull
    private Integer id;
    private String name;
    private String surName;
    private PersonType personType;
    private String email;
    private Address address;
    private LocalDate birthDate;
}
