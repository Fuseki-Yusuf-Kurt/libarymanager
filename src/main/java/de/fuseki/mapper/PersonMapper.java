package de.fuseki.mapper;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Person;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {
    Person toEntity(PersonDto personDto);

    PersonDto toDto(Person person);

    List<PersonDto> toDtoList(List<Person> personList);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Person partialUpdate(PersonDto personDto, @MappingTarget Person person);
}