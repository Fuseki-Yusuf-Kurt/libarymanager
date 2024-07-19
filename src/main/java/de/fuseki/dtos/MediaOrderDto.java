package de.fuseki.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MediaOrderDto extends OderDto{
    private LocalDate releaseDate;
}
