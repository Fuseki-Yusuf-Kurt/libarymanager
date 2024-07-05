package de.fuseki.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateOrderDto {
    private Integer bookId;
    private Integer personId;
    private LocalDate beginDate;
    private LocalDate endDate;
}
