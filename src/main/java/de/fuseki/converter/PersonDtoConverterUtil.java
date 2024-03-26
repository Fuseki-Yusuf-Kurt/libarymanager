package de.fuseki.converter;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Person;

public class PersonDtoConverterUtil {
    public static PersonDto convertPersonToPersonDto(Person person){
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setName(person.getName());
        personDto.setSurName(person.getSurName());
        personDto.setPersonType(person.getPersonType());
        personDto.setBirthDate(person.getBirthDate());
        personDto.setAddress(person.getAddress());
        personDto.setEmail(person.getEmail());
        return personDto;
    }

}
