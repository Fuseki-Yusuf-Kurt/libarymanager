package de.fuseki.service;

import de.fuseki.dtos.BookDto;
import de.fuseki.entities.Book;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.mapper.BookMapper;
import de.fuseki.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookDto> getAllBooks() {
        List<Book> foundBooks = bookRepository.findAll();
        return BookMapper.MAPPER.toDtoList(foundBooks);
    }

    public BookDto getBook(Integer id) {
        return BookMapper.MAPPER.toDto(getBookFromDatabase(id));
    }

    private Book getBookFromDatabase(int id) {
        try {
            Optional<Book> foundBook = bookRepository.findById(id);
            if (foundBook.isEmpty()) {
                throw new IdNotFoundException("Id not found!");
            }
            return foundBook.get();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IdNotFoundException("Id should not be null.");
        }
    }

    public void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new IdNotFoundException("Id not found!");
        }
        bookRepository.deleteById(id);
    }

    public BookDto addBook(BookDto inputBookDto) {
        if (inputBookDto.getId() != null) {
            throw new IdShouldBeNullException("Id should be null!");
        }
        if (inputBookDto.getTitle() == null) {
            throw new IsNullException("Title can't be Null.");
        }
        Book inoputBook = BookMapper.MAPPER.toEntity(inputBookDto);
        Book returnedBook = bookRepository.save(inoputBook);
        return BookMapper.MAPPER.toDto(returnedBook);
    }

    public BookDto updateBook(BookDto bookDto) {
        Book bookFromDatabase = getBookFromDatabase(bookDto.getId());

        BookMapper.MAPPER.partialUpdate(bookDto, bookFromDatabase);
        return BookMapper.MAPPER.toDto(bookFromDatabase);
    }
}
