package de.fuseki.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
@Converter
public class BirthDateConvert implements AttributeConverter<LocalDate,String> {
    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        return localDate.toString();

    }

    @Override
    public LocalDate convertToEntityAttribute(String s) {
        return LocalDate.parse(s);
    }
}
