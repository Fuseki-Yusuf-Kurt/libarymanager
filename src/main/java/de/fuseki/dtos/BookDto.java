package de.fuseki.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private Integer id;
    private String title;
    private String genre;
    private String author;
    private LocalDate releaseDate;
    private LocalDate lendDate;
    private LocalDate reservedDate;
    private Boolean reserved;
}
