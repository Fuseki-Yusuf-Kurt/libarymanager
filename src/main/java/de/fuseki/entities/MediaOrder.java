package de.fuseki.entities;

import de.fuseki.converter.LocalDateConvert;
import de.fuseki.enums.MediaOrderType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.lang.annotation.Inherited;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Table(name = "media_order")
public class MediaOrder extends Order {
    @Column(name = "return_date", nullable = false)
    @Convert(converter = LocalDateConvert.class)
    private LocalDate returnDate;
}