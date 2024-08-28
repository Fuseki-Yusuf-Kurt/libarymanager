package de.fuseki.mapper;

import de.fuseki.dtos.CreateMediaOrderDto;
import de.fuseki.dtos.CreateOrderDto;
import de.fuseki.dtos.MediaOrderDto;
import de.fuseki.entities.MediaOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MediaOrderMapper {

    public MediaOrder toEntity(final CreateOrderDto createOrderDto);

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "book", ignore = true)
    public MediaOrder toEntity(final CreateMediaOrderDto mediaOrderDto);

    public MediaOrderDto toDto(final MediaOrder mediaOrder);

    public List<MediaOrderDto> toDtoList(final List<MediaOrder> mediaOrders);

    public List<MediaOrder> toEntity(final List<CreateMediaOrderDto> mediaOrderDtoList);
}
