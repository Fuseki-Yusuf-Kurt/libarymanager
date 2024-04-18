package de.fuseki.service;

import de.fuseki.dtos.BookDto;
import de.fuseki.entities.Book;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.mapper.BookMapperImpl;
import de.fuseki.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    private BookService underTest;
    @Mock
    BookMapperImpl bookMapper;

    Book testBook;
    Book testBookWithoutId;
    BookDto testBookDto;
    BookDto testBookDtoWithoutId;

    @BeforeEach
    void setUp() {
        testBook = new Book(1, "testTitle", "testGenre", "testAuthor", LocalDate.parse("2000-01-01"));
        testBookWithoutId = new Book(null, "testTitle", "testGenre", "testAuthor", LocalDate.parse("2000-01-01"));
        testBookDto = new BookDto(1, "testTitle", "testGenre", "testAuthor", LocalDate.parse("2000-01-01"));
        testBookDtoWithoutId = new BookDto(null, "testTitle", "testGenre", "testAuthor", LocalDate.parse("2000-01-01"));
    }

    @Test
    void gestsAllBooks() {
        //Given
        //Mocking
        when(bookMapper.toDtoList(List.of(testBook))).thenReturn(List.of(testBookDto));
        when(bookRepository.findAll()).thenReturn(List.of(testBook));
        //When
        List<BookDto> bookDtoList = underTest.getAllBooks();
        //Then
        assertEquals(bookMapper.toDtoList(List.of(testBook)), bookDtoList);
    }

    @Test
    void deleteBookDeletsBook() {
        // Mocking
        when(bookRepository.existsById(1)).thenReturn(true);
        // When
        underTest.deleteBook(1);
        // Then
        verify(bookRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteBookThrowsIdNotFoundException() {
        // Mocking
        when(bookRepository.existsById(1)).thenReturn(false);
        // Then
        assertThrowsExactly(IdNotFoundException.class, () -> underTest.deleteBook(1));
    }

    @Test
    void addBookAddsTestBookThrowsIdShouldBeNullException() {
        // When
        assertThrowsExactly(IdShouldBeNullException.class, () -> underTest.addBook(testBookDto));
    }

    @Test
    void addBookAddsTestBookThrowsIsNullException() {
        // When
        testBookDtoWithoutId.setTitle(null);
        assertThrowsExactly(IsNullException.class, () -> underTest.addBook(testBookDtoWithoutId));
    }

    @Test
    void getOneBookThrowsIdNotFoundException() {
        // Mocking
        when(bookRepository.findById(1)).thenReturn(Optional.empty());
        // Then
        assertThrowsExactly(IdNotFoundException.class, () -> underTest.getBook(1));
    }
}