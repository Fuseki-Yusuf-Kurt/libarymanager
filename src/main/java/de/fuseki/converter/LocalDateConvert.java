package de.fuseki.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;

@Converter
public class LocalDateConvert implements AttributeConverter<LocalDate, String> {
    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.toString();

    }

    @Override
    public LocalDate convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return LocalDate.parse(s);
    }
}
