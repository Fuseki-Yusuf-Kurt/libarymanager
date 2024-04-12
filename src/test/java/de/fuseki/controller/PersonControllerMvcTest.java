package de.fuseki.controller;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Address;
import de.fuseki.enums.PersonType;
import de.fuseki.exceptions.EmailAlreadyExistsException;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.exceptions.IsNullException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonControllerMvcTest extends AbstractControllerMvc {

    @Test
    @Sql("/3-test-persons.sql")
    @Transactional
    public void deleteExistingPerson() throws Exception {
        MvcResult mvcResult = mvc.perform(delete("/user/{id}", 1))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @Sql("/3-test-persons.sql")
    @Transactional
    public void getOnePersonById() throws Exception {
        PersonDto firstPersonDtoInDatabase = new PersonDto(1, "yasin", "tulyu", PersonType.CLIENT, "yasin.tuylu001@stud.fh-dortmund.de", new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));
        MvcResult mvcResult = mvc.perform(get("/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        PersonDto resultPersonDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PersonDto.class);
        assertEquals(firstPersonDtoInDatabase, resultPersonDto);

    }

    @Test
    @Sql("/3-test-persons.sql")
    @Transactional
    public void getOnePersonByNotExistingIdThrowsException() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/user/{id}", 4))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Exception exception = mvcResult.getResolvedException();
        assertEquals(IdNotFoundException.class, exception.getClass());
    }

    @Test
    @Sql("/3-test-persons.sql")
    @Transactional
    public void putNameOfYasinToBurak() throws Exception {
        PersonDto dtoOfTheChanges = new PersonDto(1, "NewName", null, null, null, null, null);
        MvcResult mvcResult = mvc.perform(put(API_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoOfTheChanges)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        PersonDto resultDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PersonDto.class);
        assertEquals(1, resultDto.getId());
        assertEquals(dtoOfTheChanges.getName(), resultDto.getName());
    }

    @Test
    @Sql("/3-test-persons.sql")
    @Transactional
    public void putEmailToExistingEmailReturnsExceptionTest() throws Exception {
        PersonDto dtoOfTheChanges = new PersonDto(1, null, null, null, "mfischer@fuseki.de", null, null);
        MvcResult mvcResult = mvc.perform(put(API_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoOfTheChanges)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        Exception exception = mvcResult.getResolvedException();
        assertEquals(EmailAlreadyExistsException.class, exception.getClass());
    }

    @Test
    @Sql("/3-test-persons.sql")
    @Transactional
    public void getAllPersonsAPITestShouldReturn3Objects() throws Exception {
        MvcResult mvcResult = mvc.perform(get(API_USERS)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();


        List<PersonDto> returnedPersonList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, PersonDto.class));

        assertEquals(3, returnedPersonList.size());
    }

    @Test
    @Transactional
    public void postOnePerson() throws Exception {
        //Given
        PersonDto personDto = new PersonDto(null, "yasin", "tulyu", PersonType.CLIENT, "yasin.tuylu001@stud.fh-dortmund.de", new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));

        MvcResult result = mvc
                .perform(
                        post(API_USER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        PersonDto returnedDtoFromTestedMethod = objectMapper.readValue(result.getResponse().getContentAsString(), PersonDto.class);


        MvcResult mvcResult = mvc.perform(get("/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        PersonDto personDtoResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PersonDto.class);

        assertEquals(returnedDtoFromTestedMethod, personDtoResult);
        assertEquals(personDto.getName(), personDtoResult.getName());
        assertEquals(personDto.getSurName(), personDtoResult.getSurName());
        assertEquals(personDto.getEmail(), personDtoResult.getEmail());
        assertEquals(personDto.getPersonType(), personDtoResult.getPersonType());
        assertEquals(personDto.getAddress(), personDtoResult.getAddress());
        assertEquals(personDto.getBirthDate(), personDtoResult.getBirthDate());
        assertEquals(1, personDtoResult.getId());
    }

    @Test
    @Transactional
    public void postOnePersonReturnsIdNullException() throws Exception {
        //Given
        PersonDto personDto = new PersonDto(1, "yasin", "tulyu", PersonType.CLIENT, "yasin.tuylu001@stud.fh-dortmund.de", new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));

        MvcResult result = mvc
                .perform(
                        post(API_USER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Exception returnedException = result.getResolvedException();
        assertNotNull(returnedException);
        assertEquals(IdShouldBeNullException.class, returnedException.getClass());
    }

    @Test
    @Transactional
    @Sql("/3-test-persons.sql")
    public void postOnePersonReturnsEmailAlreadyExistsException() throws Exception {
        PersonDto personDto = new PersonDto(null, "yasin", "tulyu", PersonType.CLIENT, "yasin.tuylu001@stud.fh-dortmund.de", new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));

        MvcResult result = mvc
                .perform(
                        post(API_USER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Exception returnedException = result.getResolvedException();
        assertNotNull(returnedException);
        assertEquals(EmailAlreadyExistsException.class, returnedException.getClass());
    }

    @ParameterizedTest(name = "{0}")
    @Transactional
    @Sql("/3-test-persons.sql")
    @MethodSource("provideTestData")
    public void postOnePersonReturnsIsNullExceptionBecauseNameIsNull(String testName, PersonDto testPersonDto) throws Exception {
        MvcResult result = mvc
                .perform(
                        post(API_USER)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testPersonDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Exception returnedException = result.getResolvedException();
        assertNotNull(returnedException);
        assertEquals(IsNullException.class, returnedException.getClass());
    }

    private static Stream<Arguments> provideTestData(){
        return Stream.of(
                Arguments.of(
                        "name = null throws IsNullException." ,
                        PersonDto.builder()
                                .name(null)
                                .surName("notNull")
                                .personType(PersonType.CLIENT)
                                .email("NotNullAndNotExisting@email.de")
                                .address(new Address("karlstr", "33", "essen", "45666"))
                                .birthDate(LocalDate.parse("2000-11-11"))
                                .build()),
                Arguments.of(
                        "surName = null throws IsNullException.",
                        new PersonDto(
                                null,
                                "notNullName",
                                null,
                                PersonType.CLIENT,
                                "NotNullAndNotExisting@email.de",
                                new Address("karlstr", "33", "essen", "45666"),
                                LocalDate.parse("2004-12-28"))),
                Arguments.of(
                        "personType = null throws IsNullException.",
                        new PersonDto(
                                null,
                                "notNullName",
                                "notNullSurname",
                                null,
                                "NotNullAndNotExisting@email.de",
                                new Address("karlstr", "33", "essen", "45666"),
                                LocalDate.parse("2004-12-28"))),
                Arguments.of(
                        "email = null throws IsNullException.",
                        new PersonDto(
                                null,
                                "notNullName",
                                "notNullSurname",
                                PersonType.CLIENT,
                                null,
                                new Address("karlstr", "33", "essen", "45666"),
                                LocalDate.parse("2004-12-28"))),
                Arguments.of(
                        "address = null throws IsNullException.",
                        new PersonDto(
                                null,
                                "notNullName",
                                "notNullSurname",
                                PersonType.CLIENT,
                                "NotNullAndNotExisting@email.de",
                                null,
                                LocalDate.parse("2004-12-28"))),
                Arguments.of(
                        "birthDate = null throws IsNullException.",
                        new PersonDto(
                                null,
                                "notNullName",
                                "notNullSurname",
                                PersonType.CLIENT,
                                "NotNullAndNotExisting@email.de",
                                new Address("karlstr", "33", "essen", "45666"),
                                null)));
    }


}
