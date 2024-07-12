package de.fuseki.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {
    private int id;
    private int bookId;
    private int userId;
    private LocalDate endDate;
}
