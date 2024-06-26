package de.fuseki.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
}
