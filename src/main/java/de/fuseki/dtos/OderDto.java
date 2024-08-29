package de.fuseki.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Data
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
