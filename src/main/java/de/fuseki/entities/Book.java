package de.fuseki.entities;

import de.fuseki.converter.LocalDateConvert;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "genre")
    private String genre;
    @Column(name = "address")
    private String author;
    @Column(name = "release_date")
    @Convert(converter = LocalDateConvert.class)
    private LocalDate releaseDate;
}