package de.fuseki.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {
    private int id;
    private int bookId;
    private int userId;
    private LocalDate endDate;
}
