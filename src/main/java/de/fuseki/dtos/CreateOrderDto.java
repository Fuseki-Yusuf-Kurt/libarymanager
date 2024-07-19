package de.fuseki.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class CreateOrderDto {
    private Integer id;
    private Integer bookId;
    private Integer personId;
    private LocalDate beginDate;
    private LocalDate endDate;
}
