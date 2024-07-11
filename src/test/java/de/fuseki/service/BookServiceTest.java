package de.fuseki.service;

import de.fuseki.dtos.BookDto;
import de.fuseki.entities.Book;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.mapper.BookMapper;
import de.fuseki.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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

    Book testBook;
    Book testBookWithoutId;
    BookDto testBookDto;
    BookDto testBookDtoWithoutId;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1);
        testBook.setAuthor("testAuthor");
        testBook.setTitle("testTitle");
        testBook.setGenre("testGenre");
        testBook.setReleaseDate(LocalDate.parse("2000-01-01"));

        testBookWithoutId = new Book();
        testBookWithoutId.setId(null);
        testBookWithoutId.setAuthor("testAuthor");
        testBookWithoutId.setTitle("testTitle");
        testBookWithoutId.setGenre("testGenre");
        testBookWithoutId.setReleaseDate(LocalDate.parse("2000-01-01"));

        testBookDto = new BookDto();
        testBookDto.setId(1);
        testBookDto.setAuthor("testAuthor");
        testBookDto.setTitle("testTitle");
        testBookDto.setGenre("testGenre");
        testBookDto.setReleaseDate(LocalDate.parse("2000-01-01"));

        testBookDtoWithoutId = new BookDto();
        testBookDtoWithoutId.setId(null);
        testBookDtoWithoutId.setAuthor("testAuthor");
        testBookDtoWithoutId.setTitle("testTitle");
        testBookDtoWithoutId.setGenre("testGenre");
        testBookDtoWithoutId.setReleaseDate(LocalDate.parse("2000-01-01"));
    }

    @Test
    void gestsAllBooks() {
        //Given
        //Mocking
        when(bookRepository.findAll()).thenReturn(List.of(testBook));
        //When
        List<BookDto> bookDtoList = underTest.getAllBooks();
        //Then
        assertEquals(BookMapper.MAPPER.toDtoList(List.of(testBook)), bookDtoList);
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

    @Test
    void updateBook() {
        BookDto updateDto = new BookDto();
        updateDto.setId(1);
        updateDto.setTitle("newTitle");
        updateDto.setGenre("newGenre");
        // Mocking
        when(bookRepository.findById(1)).thenReturn(Optional.of(testBook));
        // When
        BookDto returnedDto = underTest.updateBook(updateDto);

        // Then
        BookDto updatedDto = new BookDto();
        updatedDto.setId(1);
        updatedDto.setTitle("newTitle");
        updatedDto.setGenre("newGenre");
        updatedDto.setAuthor(testBook.getAuthor());
        updatedDto.setReleaseDate(testBook.getReleaseDate());

        assertEquals(updatedDto, returnedDto);
    }
}