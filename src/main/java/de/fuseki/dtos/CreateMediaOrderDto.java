package de.fuseki.dtos;

import de.fuseki.enums.MediaOrderType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMediaOrderDto {
    private Integer bookId;
    private Integer personId;
    private LocalDate beginDate;
    private LocalDate endDate;
    private MediaOrderType type;
}
