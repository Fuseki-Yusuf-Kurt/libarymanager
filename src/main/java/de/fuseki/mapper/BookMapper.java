package de.fuseki.mapper;

import de.fuseki.dtos.BookDto;
import de.fuseki.entities.Book;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    Book toEntity(BookDto bookDto);

    BookDto toDto(Book book);

    List<BookDto> toDtoList(List<Book> bookList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book partialUpdate(BookDto bookDto, @MappingTarget() Book book);
}