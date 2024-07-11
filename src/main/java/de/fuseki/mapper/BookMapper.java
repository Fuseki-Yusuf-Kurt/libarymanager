package de.fuseki.mapper;

import de.fuseki.dtos.BookDto;
import de.fuseki.entities.Book;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    BookMapper MAPPER = Mappers.getMapper(BookMapper.class);
    Book toEntity(BookDto bookDto);

    BookDto toDto(Book book);

    List<BookDto> toDtoList(List<Book> bookList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "busyDate", ignore = true)
    Book partialUpdate(BookDto bookDto, @MappingTarget() Book book);
}