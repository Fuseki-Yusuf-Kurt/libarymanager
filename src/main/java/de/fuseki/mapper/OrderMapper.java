package de.fuseki.mapper;

import de.fuseki.dtos.CreateMediaOrderDto;
import de.fuseki.dtos.OrderDto;
import de.fuseki.entities.MediaOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    MediaOrder toEntity(final OrderDto orderDto);

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "book", ignore = true)
    MediaOrder toEntity(final CreateMediaOrderDto createMediaOrderDto);
}
