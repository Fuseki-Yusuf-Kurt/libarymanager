package de.fuseki.converter;

import de.fuseki.entities.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressConverterTest {

    Address address = new Address("test street", "test number", "test city", "test postal code");
    AddressConverter testConverter = new AddressConverter();
    @Test
    void convertToDatabaseColumn() {
        // When
        String result = testConverter.convertToDatabaseColumn(address);
        // Then
        assertEquals( "test street, test number, test city, test postal code",result);
    }

    @Test
    void convertToEntityAttribute() {
        // Given
        String given = "test street, test number, test city, test postal code";
        //When
        Address result = testConverter.convertToEntityAttribute(given);
        //Then
        assertEquals(address,result);
    }
}