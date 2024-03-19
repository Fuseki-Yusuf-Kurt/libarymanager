package de.fuseki.entities;


import de.fuseki.enums.PersonType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Person {
    @Id
    private int id;
    private String name;
    private String surname;
    private PersonType personType;
    private String email;
    private String address;
    private LocalDate birthDate;

}
