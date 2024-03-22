package de.fuseki.converter;

import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class BirthDateConvertTest {

    LocalDate testDate = LocalDate.parse("2000-01-01");

    BirthDateConvert testConvert = new BirthDateConvert();

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