package de.fuseki.mapper;

import de.fuseki.dtos.ReservationDto;
import de.fuseki.entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    ReservationMapper MAPPER = Mappers.getMapper(ReservationMapper.class);


    public Reservation toEntity(ReservationDto reservationDto);

    public ReservationDto toDto(Reservation reservation);

    public List<ReservationDto> toDtoList(List<Reservation> reservations);
}
