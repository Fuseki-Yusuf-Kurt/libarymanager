package de.fuseki.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class OderDto {
    private int id;
    private PersonDto person;
    private BookDto book;
    private LocalDate beginDate;
    private LocalDate endDate;
}
