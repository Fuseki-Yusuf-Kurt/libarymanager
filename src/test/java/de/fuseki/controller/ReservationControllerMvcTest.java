package de.fuseki.controller;

import de.fuseki.dtos.BookDto;
import de.fuseki.dtos.PersonDto;
import de.fuseki.dtos.ReservationDto;
import de.fuseki.entities.Address;
import de.fuseki.enums.PersonType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationControllerMvcTest extends AbstractControllerMvc {
    PersonDto testPersonDto = PersonDto.builder()
            .id(1)
            .email("yasin.tuylu001@stud.fh-dortmund.de")
            .name("yasin")
            .surName("tulyu")
            .personType(PersonType.CLIENT)
            .birthDate(LocalDate.parse("2004-12-28"))
            .address(Address.builder()
                    .city("essen")
                    .houseNumber("33")
                    .postalCode("45666")
                    .street("karlstr")
                    .build())
            .build();
    BookDto testBook = BookDto.builder()
            .id(1)
            .reserved(false)
            .lended(false)
            .title("testTitle1")
            .genre("testGenre1")
            .author("testAuthor1")
            .releaseDate(LocalDate.parse("2001-01-01"))
            .reservedDate(LocalDate.parse("2024-09-08"))
            .build();

    @Test
    @Sql("/3-test-Books.sql")
    @Sql("/3-test-persons.sql")
    public void reservationAddingMvcTestWithCorrectInput() throws Exception {
        ReservationDto reservationDto = ReservationDto.builder()
                .endDate(LocalDate.now().plusDays(10))
                .book(BookDto.builder().id(1).build())
                .person(PersonDto.builder().id(1).build())
                .build();
        MvcResult mvcResult = mvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk())
                .andReturn();
        ReservationDto returnedDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationDto.class);

        assertEquals(returnedDto.getEndDate(), reservationDto.getEndDate());
        assertEquals(testBook, returnedDto.getBook());
        assertEquals(testPersonDto, returnedDto.getPerson());
        assertEquals(LocalDate.now().getDayOfYear(), returnedDto.getBeginDate().getDayOfYear());
    }
}
