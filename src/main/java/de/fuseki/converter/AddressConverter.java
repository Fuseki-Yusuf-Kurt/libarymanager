package de.fuseki.converter;

import de.fuseki.entities.Address;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AddressConverter implements AttributeConverter<Address, String> {
    @Override
    public String convertToDatabaseColumn(Address address) {
        if (address != null && address.getPostalCode() != null && address.getCity() != null && address.getStreet() != null && address.getHouseNumber() != null) {
            return address.getStreet() + ", " + address.getHouseNumber() + ", " + address.getCity() + ", " + address.getPostalCode();
        }else return null;
    }

    @Override
    public Address convertToEntityAttribute(String s) {
        Address generatedAddress;
        String[] splittedString = s.split(", ");
        if(splittedString.length == 4) {
            generatedAddress = new Address(
                    splittedString[0],
                    splittedString[1],
                    splittedString[2],
                    splittedString[3]
            );
        }else return null;
        return generatedAddress;
    }
}
