package de.fuseki.mapper;

import de.fuseki.dtos.CreateOrderDto;
import de.fuseki.dtos.OrderDto;
import de.fuseki.entities.Order;
import de.fuseki.entities.Person;
import de.fuseki.service.PersonService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    Order toEntity(final OrderDto orderDto);

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "book", ignore = true)
    Order toEntity(final CreateOrderDto createOrderDto);
}
