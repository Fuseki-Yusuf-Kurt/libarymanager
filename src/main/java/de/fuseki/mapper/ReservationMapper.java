package de.fuseki.mapper;

import de.fuseki.dtos.CreateReservationDto;
import de.fuseki.dtos.ReservationDto;
import de.fuseki.entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    ReservationMapper MAPPER = Mappers.getMapper(ReservationMapper.class);
@Mapping(target = "personId" , expression = "java(reservation.getPerson().getId())")
@Mapping(target = "bookId" , expression = "java(reservation.getBook().getId())")
    public CreateReservationDto toCreateDto(Reservation reservation);

    @Mapping(target = "person", ignore = true)
    @Mapping(target = "book", ignore = true)
    public Reservation toEntity(CreateReservationDto reservationDto);

    public ReservationDto toDto(Reservation reservation);
}
