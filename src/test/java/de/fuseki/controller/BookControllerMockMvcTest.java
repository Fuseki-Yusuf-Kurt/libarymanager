package de.fuseki.controller;

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
        testBook1 = new BookDto();
        testBook1.setId(1);
        testBook1.setTitle("testTitle1");
        testBook1.setAuthor("testAuthor1");
        testBook1.setGenre("testGenre1");
        testBook1.setReleaseDate(LocalDate.parse("2001-01-01"));

        testBook1WithoutId = new BookDto();
        testBook1WithoutId.setTitle("testTitle1");
        testBook1WithoutId.setAuthor("testAuthor1");
        testBook1WithoutId.setGenre("testGenre1");
        testBook1WithoutId.setReleaseDate(LocalDate.parse("2001-01-01"));

        testBook2 = new BookDto();
        testBook2.setId(2);
        testBook2.setTitle("testTitle2");
        testBook2.setAuthor("testAuthor2");
        testBook2.setGenre("testGenre2");
        testBook2.setReleaseDate(LocalDate.parse("2002-02-02"));

        testBook2WithoutId = new BookDto();
        testBook2WithoutId.setTitle("testTitle2");
        testBook2WithoutId.setAuthor("testAuthor2");
        testBook2WithoutId.setGenre("testGenre2");
        testBook2WithoutId.setReleaseDate(LocalDate.parse("2002-02-02"));

        testBook3 = new BookDto();
        testBook3.setId(3);
        testBook3.setTitle("testTitle3");
        testBook3.setAuthor("testAuthor3");
        testBook3.setGenre("testGenre3");
        testBook3.setReleaseDate(LocalDate.parse("2003-03-03"));

    }

    @Test
    @Sql("/3-test-Books.sql")
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
    @Sql("/3-test-Books.sql")
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
    @Sql("/3-test-Books.sql")
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
    @Sql("/3-test-Books.sql")
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
    @Sql("/3-test-Books.sql")
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
    @Sql("/3-test-Books.sql")
    public void mvcTestUpdateBook() throws Exception {
        BookDto updateDto = new BookDto();
        updateDto.setId(1);
        updateDto.setTitle("newTitle1");
        updateDto.setGenre("newGenre1");
        updateDto.setAuthor("newAuthor");
        updateDto.setReleaseDate(LocalDate.parse("2005-05-05"));
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
