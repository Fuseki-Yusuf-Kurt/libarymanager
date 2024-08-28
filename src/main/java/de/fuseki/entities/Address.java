package de.fuseki.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Address {
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
}
