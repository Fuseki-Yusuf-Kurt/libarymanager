package de.fuseki.converter;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateConvertTest {

    LocalDate testDate = LocalDate.parse("2000-01-01");

    LocalDateConvert testConvert = new LocalDateConvert();

    @Test
    void convertToDatabaseColumnTest() {
        //When
        String result = testConvert.convertToDatabaseColumn(testDate);
        //Then
        assertEquals(result,"2000-01-01");
    }

    @Test
    void convertToEntityAttributeTest() {
        //When
        LocalDate testDate = testConvert.convertToEntityAttribute("2000-01-01");
        //Then
        assertEquals(testDate.getYear(),2000);
        assertEquals(testDate.getMonth(), Month.JANUARY);
        assertEquals(testDate.getDayOfMonth(),1);
    }
}