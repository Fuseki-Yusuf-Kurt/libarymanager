package de.fuseki.entities;

import de.fuseki.converter.BooleanConverter;
import de.fuseki.converter.LocalDateConvert;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "genre")
    private String genre;
    @Column(name = "author")
    private String author;
    @Column(name = "release_date")
    @Convert(converter = LocalDateConvert.class)
    private LocalDate releaseDate;
    @Column(name = "lend_date")
    @Convert(converter = LocalDateConvert.class)
    private LocalDate lendDate;
    @Convert(converter = LocalDateConvert.class)
    @Column(name = "reserved_date")
    private LocalDate reservedDate;
    @Convert(converter = BooleanConverter.class)
    @Column(name = "reserved")
    private Boolean reserved;

}