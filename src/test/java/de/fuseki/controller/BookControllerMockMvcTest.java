package de.fuseki.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.fuseki.dtos.BookDto;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.exceptions.IsNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerMockMvcTest extends AbstractControllerMvc {
    BookDto testBook1;
    BookDto testBook1WithoutId;
    BookDto testBook2;
    BookDto testBook2WithoutId;
    BookDto testBook3;

    @BeforeEach
    void setUp() {
        testBook1 = new BookDto(1, "testTitle1", "testGenre1", "testAuthor1", LocalDate.parse("2001-01-01"));
        testBook1WithoutId = new BookDto(null, "testTitle1", "testGenre1", "testAuthor1", LocalDate.parse("2001-01-01"));
        testBook2 = new BookDto(2, "testTitle2", "testGenre2", "testAuthor2", LocalDate.parse("2002-02-02"));
        testBook2WithoutId = new BookDto(null, "testTitle2", "testGenre2", "testAuthor2", LocalDate.parse("2002-02-02"));
        testBook3 = new BookDto(3, "testTitle3", "testGenre3", "testAuthor3", LocalDate.parse("2003-03-03"));
    }

    @Test
    @Sql("/3-test-books.sql")
    public void mvcTestGettingAllBooks() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/books"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        List<BookDto> returnedBookDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class));

        assertEquals(testBook1, returnedBookDtos.getFirst());
        assertEquals(testBook2, returnedBookDtos.get(1));
        assertEquals(testBook3, returnedBookDtos.get(2));
    }

    @Test
    @Sql("/3-test-books.sql")
    public void mvcTestGettingOneBook() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/book/{id}", testBook1.getId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        BookDto returnedBookDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(testBook1, returnedBookDto);
    }

    @Test
    @Sql("/3-test-books.sql")
    public void mvcTestGettingOneBookByNotExistingIdReturnsIdNotFoundException() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/book/{id}", 100))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        Exception exception = mvcResult.getResolvedException();

        assertNotNull(exception);
        assertEquals(IdNotFoundException.class, exception.getClass());
    }

    @Test
    @Sql("/3-test-books.sql")
    public void mvcTestDeletingBook() throws Exception {
        MvcResult mvcResult = mvc.perform(delete("/book/{id}", testBook1.getId()))
                .andExpectAll(
                        status().isNoContent()
                ).andReturn();

        MvcResult checkResult = mvc.perform(get("/book/{id}", testBook1.getId()))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        Exception exception = checkResult.getResolvedException();

        assertNotNull(exception);
        assertEquals(IdNotFoundException.class, exception.getClass());
    }

    @Test
    @Sql("/3-test-books.sql")
    public void mvcTestDeletingBookThrowsIdNotFoundException() throws Exception {
        MvcResult mvcResult = mvc.perform(delete("/book/{id}", 100))
                .andExpectAll(
                        status().isNotFound()
                ).andReturn();


        Exception exception = mvcResult.getResolvedException();

        assertNotNull(exception);
        assertEquals(IdNotFoundException.class, exception.getClass());
    }

    @Test
    public void mvcTestAddPerson() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook1WithoutId)))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        BookDto returnedBookDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(testBook1, returnedBookDto);

        MvcResult checkResult = mvc.perform(get("/book/{id}", testBook1.getId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        BookDto checkedDto = objectMapper.readValue(checkResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(testBook1, returnedBookDto);
    }

    @Test
    public void mvcTestAddPersonThrowsIdShouldBeNullException() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook1)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        Exception exception = mvcResult.getResolvedException();

        assertNotNull(exception);
        assertEquals(IdShouldBeNullException.class, exception.getClass());
    }

    @Test
    public void mvcTestAddPersonThrowsIsNullException() throws Exception {
        testBook1WithoutId.setTitle(null);

        MvcResult mvcResult = mvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook1WithoutId)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        Exception exception = mvcResult.getResolvedException();

        assertNotNull(exception);
        assertEquals(IsNullException.class, exception.getClass());
    }

    @Test
    @Sql("/3-test-books.sql")
    public void mvcTestUpdateBook() throws Exception {
        BookDto updateDto = new BookDto(1, "newTitle1", "newGenre1", "newAuthor1", LocalDate.parse("2005-05-05"));
        MvcResult mvcResult = mvc.perform(put("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        MvcResult checkResult = mvc.perform(get("/book/{id}", testBook1.getId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andReturn();

        BookDto returnedBookDtoFromPut = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        BookDto returnedBookDtoFromCheck = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(updateDto, returnedBookDtoFromPut);
        assertEquals(updateDto, returnedBookDtoFromCheck);
    }

}
