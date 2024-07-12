package de.fuseki.entities;

import de.fuseki.converter.LocalDateConvert;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "book_id", nullable = false)
    private int bookId;
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Convert(converter = LocalDateConvert.class)
    @Column(name = "end_date", nullable = false, columnDefinition = "TEXT")
    private LocalDate endDate;
}
