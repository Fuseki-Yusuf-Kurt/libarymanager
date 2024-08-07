package de.fuseki.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateMediaOrderDto extends CreateOrderDto {
    private LocalDate returnDate;
}
