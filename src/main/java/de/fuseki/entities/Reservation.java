package de.fuseki.entities;

import de.fuseki.converter.LocalDateConvert;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation extends Order{
}
