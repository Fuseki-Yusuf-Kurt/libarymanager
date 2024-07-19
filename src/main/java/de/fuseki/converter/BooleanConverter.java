package de.fuseki.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Boolean aBoolean) {
        if (aBoolean == null) return 0;
        if (aBoolean){
            return 1;
        } else return 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer integer) {
        if (integer == null) return null;
        if (integer == 1) {
            return true;
        } else if (integer == 0 ){
            return false;
        } else return null;
    }
}
