package de.fuseki.service;

import de.fuseki.dtos.BookDto;
import de.fuseki.entities.Book;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.exceptions.NoAccesException;
import de.fuseki.mapper.BookMapper;
import de.fuseki.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
// TODO: Bücher könnne nicht gelöscht werden und Persohnen können nicht gelöscht werden Implementieren.
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<BookDto> getAllBooks() {
        List<Book> foundBooks = bookRepository.findAll();
        return BookMapper.MAPPER.toDtoList(foundBooks);
    }

    public BookDto getBook(Integer id) {
        Book requestedBook = getBookFromDatabase(id);
        BookDto requestedBookDto = BookMapper.MAPPER.toDto(requestedBook);
        requestedBookDto.setLendDate(null);
        requestedBookDto.setReservedDate(null);
        return requestedBookDto;
    }

    public Book getBookFromDatabase(int id) {
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
        Book searchedBook = getBookFromDatabase(id);
        if(searchedBook.getLended() || searchedBook.getReserved()) {
            throw new NoAccesException("Book is not reserved or lent.");
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
        Book inputBook = BookMapper.MAPPER.toEntity(inputBookDto);
        Book returnedBook = bookRepository.save(inputBook);
        return BookMapper.MAPPER.toDto(returnedBook);
    }

    public BookDto updateBook(BookDto bookDto) {
        Book bookFromDatabase = getBookFromDatabase(bookDto.getId());

        BookMapper.MAPPER.partialUpdate(bookDto, bookFromDatabase);
        return BookMapper.MAPPER.toDto(bookFromDatabase);
    }
}
