package de.fuseki.service;

import de.fuseki.dtos.BookDto;
import de.fuseki.dtos.CreateReservationDto;
import de.fuseki.entities.Address;
import de.fuseki.entities.Book;
import de.fuseki.entities.Person;
import de.fuseki.entities.Reservation;
import de.fuseki.enums.PersonType;
import de.fuseki.exceptions.DateNotValidException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
@MockitoSettings(strictness = Strictness.LENIENT)
class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private PersonService personService;
    @Mock
    private BookService bookService;
    @Mock
    private ReservationRepository reservationRepository;
    Book testBookNotLendedNotReserved;
    BookDto testBookNotLendedNotReservedDto;
    Book testBookLendedNotReserved;
    BookDto testBookLendedNotReservedDto;
    Book testBookNotLendedButReserved;
    BookDto testBookNotLendedButReservedDto;
    Book testBookLendedAndReserved;
    BookDto testBookLendedAndReservedDto;

    Person testPerson1;

    @BeforeEach
    void setUp() {
        testBookNotLendedNotReserved = Book.builder()
                .id(1)
                .title("test title")
                .genre("testGenre")
                .author("test author")
                .releaseDate(LocalDate.parse("2000-12-12"))
                .reserved(false)
                .build();
        testBookNotLendedNotReservedDto = BookDto.builder()
                .id(1)
                .title("test title")
                .genre("testGenre")
                .author("test author")
                .releaseDate(LocalDate.parse("2000-12-12"))
                .reserved(false)
                .build();

        testBookLendedNotReserved = Book.builder()
                .id(2)
                .title("test title")
                .genre("testGenre")
                .author("test author")
                .releaseDate(LocalDate.parse("2000-12-12"))
                .lendDate(LocalDate.now().plusWeeks(1))
                .reserved(false)
                .build();

        testBookLendedNotReservedDto = BookDto.builder()
                .id(2)
                .title("test title")
                .genre("testGenre")
                .author("test author")
                .releaseDate(LocalDate.parse("2000-12-12"))
                .lendDate(LocalDate.now().plusWeeks(1))
                .reserved(false)
                .build();
        testBookNotLendedButReserved = Book.builder()
                .id(3)
                .title("test title")
                .genre("testGenre")
                .author("test author")
                .reservedDate(LocalDate.now().plusWeeks(1))
                .reserved(true)
                .build();
        testBookLendedAndReserved = Book.builder()
                .id(4)
                .title("test title")
                .genre("testGenre")
                .author("test author")
                .lendDate(LocalDate.now().plusWeeks(3))
                .reserved(true)
                .build();
        testPerson1 = Person.builder()
                .id(1)
                .name("test name")
                .surName("test surname")
                .personType(PersonType.ADMIN)
                .email("testmail@mail.mail")
                .birthDate(LocalDate.parse("2003-12-12"))
                .address(new Address("teststreet", "1", "herne", "44628"))
                .build();
    }


    @ParameterizedTest(name = "{0}")
    @Transactional
    @MethodSource("addReservationTestData")
    void addReservationDataValidationTest(String testName, int person, CreateReservationDto insertedDto, Class exceptionClass) throws Exception {
        // Mocking
        switch (person) {
            case 1:
                when(bookService.getBookFromDatabase(insertedDto.getBookId()))
                        .thenReturn(testBookNotLendedNotReserved);
                break;
            case 2:
                when(bookService.getBookFromDatabase(insertedDto.getBookId()))
                        .thenReturn(testBookLendedNotReserved);
                break;
            case 3:
                when(bookService.getBookFromDatabase(insertedDto.getBookId()))
                        .thenReturn(testBookNotLendedButReserved);
                break;
            case 4:
                when(bookService.getBookFromDatabase(insertedDto.getBookId()))
                        .thenReturn(testBookLendedAndReserved);
                break;
        }
        when(personService.getPersonFromDatabase(insertedDto.getPersonId())).thenReturn(testPerson1);

        assertThrows(exceptionClass, () -> reservationService.addReservation(insertedDto));
    }

    @Test
    void addReservationTestingNotLendedAndNotReserved() {
        //WHEN
        CreateReservationDto reservationDto = CreateReservationDto.builder()
                .bookId(1)
                .personId(1)
                .endDate(LocalDate.now().plusDays(7))
                .build();
        Reservation saveReservation = Reservation.builder()
                .endDate(reservationDto.getEndDate())
                .book(testBookNotLendedNotReserved)
                .person(testPerson1)
                .build();
        Reservation returnedReservation = Reservation.builder()
                .id(1)
                .endDate(reservationDto.getEndDate())
                .book(testBookNotLendedNotReserved)
                .person(testPerson1)
                .build();

        //MOCK
        when(bookService.getBookFromDatabase(testBookNotLendedNotReservedDto.getId()))
                .thenReturn(testBookNotLendedNotReserved);
        when(personService.getPersonFromDatabase(reservationDto.getPersonId())).thenReturn(testPerson1);

        when(reservationRepository.save((saveReservation))).thenReturn(returnedReservation);
        // WHEN
        reservationService.addReservation(reservationDto);

        // Then
        assertEquals(reservationDto.getEndDate(), testBookNotLendedNotReserved.getReservedDate());
    }

    @Test
    void addReservationTestingIfReservatedIsSetToTrue(){
        //WHEN
        CreateReservationDto reservationDto = CreateReservationDto.builder()
                .bookId(1)
                .personId(1)
                .build();
        Reservation saveReservation = Reservation.builder()
                .endDate(reservationDto.getEndDate())
                .book(testBookNotLendedNotReserved)
                .person(testPerson1)
                .build();
        Reservation returnedReservation = Reservation.builder()
                .id(1)
                .endDate(reservationDto.getEndDate())
                .book(testBookNotLendedNotReserved)
                .person(testPerson1)
                .build();

        //MOCK
        when(bookService.getBookFromDatabase(testBookNotLendedNotReservedDto.getId()))
                .thenReturn(testBookLendedNotReserved);
        when(personService.getPersonFromDatabase(reservationDto.getPersonId())).thenReturn(testPerson1);

        when(reservationRepository.save((saveReservation))).thenReturn(returnedReservation);

        //WHEN
        reservationService.addReservation(reservationDto);

        //THEN
        assertTrue(testBookLendedNotReserved.getReserved());
    }

    public static Stream<Arguments> addReservationTestData() {
        return Stream.of(
                Arguments.of("Reservation id is set so exception is thrown.",
                        1,
                        CreateReservationDto.builder()
                                .endDate(LocalDate.now().plusDays(7))
                                .id(1)
                                .bookId(1)
                                .personId(1)
                                .build(),
                        IdShouldBeNullException.class)
                ,
                Arguments.of("Begin date is not null throws exception.",
                        1,
                        CreateReservationDto.builder()
                                .endDate(LocalDate.now().plusDays(7))
                                .beginDate(LocalDate.now())
                                .bookId(1)
                                .personId(1)
                                .build(),
                        DateNotValidException.class)
                ,
                Arguments.of("End date is null throws exception.",
                        1,
                        CreateReservationDto.builder()
                                .endDate(null)
                                .bookId(1)
                                .personId(1)
                                .build(),
                        DateNotValidException.class)
                ,
                Arguments.of("End date is in the past throws exception.",
                        1,
                        CreateReservationDto.builder()
                                .endDate(LocalDate.now().minusDays(10))
                                .bookId(1)
                                .personId(1)
                                .build(),
                        DateNotValidException.class)
                ,
                Arguments.of("End date is more than two weeks after now throws exception.",
                        1,
                        CreateReservationDto.builder()
                                .endDate(LocalDate.now().plusDays(15))
                                .bookId(1)
                                .personId(1)
                                .build(),
                        DateNotValidException.class)
                ,
                Arguments.of("End date is not null throws exception.",
                        2,
                        CreateReservationDto.builder()
                                .endDate(LocalDate.now().plusDays(2))
                                .bookId(1)
                                .personId(1)
                                .build(),
                        DateNotValidException.class)
                ,
                Arguments.of("Test if 3rd testcase works.",
                        3,
                        CreateReservationDto.builder()
                                .endDate(LocalDate.now().plusDays(2))
                                .bookId(1)
                                .personId(1)
                                .build(),
                        NullPointerException.class)
        );
    }
}