package de.fuseki.service;

import de.fuseki.dtos.CreateReservationDto;
import de.fuseki.dtos.ReservationDto;
import de.fuseki.entities.Book;
import de.fuseki.entities.Person;
import de.fuseki.entities.Reservation;
import de.fuseki.exceptions.DateNotValidException;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.mapper.ReservationMapper;
import de.fuseki.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final PersonService personService;
    private final BookService bookService;

    public CreateReservationDto addReservation(CreateReservationDto reservationDto) {
        if (reservationDto.getId() != null) {
            throw new IdShouldBeNullException("Reservation id is not null.");
        }

        if (reservationDto.getBeginDate() != null) {
            throw new DateNotValidException("Begin date is not null.");
        }

        Person person = personService.getPersonFromDatabase(reservationDto.getPersonId());
        Book book = bookService.getBookFromDatabase(reservationDto.getBookId());
        Reservation reservation = ReservationMapper.MAPPER.toEntity(reservationDto);
        reservation.setBook(book);
        reservation.setPerson(person);
        CreateReservationDto returnedReservationDto = null;
        if (book.getLendDate() == null && book.getReservedDate() == null && !book.getReserved()) {
            returnedReservationDto = reservate(reservation);
        } else if (book.getLendDate() != null
                && book.getReservedDate() == null
                && !book.getReserved()) {
            if (reservation.getEndDate() != null) {
                throw new DateNotValidException("End date is not null.");
            }
            book.setReserved(true);
            returnedReservationDto = ReservationMapper.MAPPER.toCreateDto(reservationRepository.save(reservation));

        } else  {
            throw new NullPointerException("Book is not available.");
        }
        return returnedReservationDto;
    }

    private CreateReservationDto reservate(Reservation reservation) {
        LocalDate fromDate = LocalDate.now();
        if (reservation.getEndDate() == null) {
            throw new DateNotValidException("End date is null.");
        }
        if (reservation.getEndDate().isBefore(fromDate)) {
            throw new DateNotValidException(
                    "Reservations only possible from " + fromDate.toString() + ".");
        }
        if (reservation.getEndDate().isAfter(fromDate.plusWeeks(2))) {
            throw new DateNotValidException("It´s Only possible to reservate up to 2 weeks.");
        }

        reservation.getBook().setReservedDate(reservation.getEndDate());
        Reservation returnedReservation = reservationRepository.save(reservation);
        CreateReservationDto returnedReservationDto = ReservationMapper.MAPPER.toCreateDto(returnedReservation);
        returnedReservationDto.setBookId(returnedReservation.getBook().getId());
        returnedReservationDto.setPersonId(returnedReservation.getPerson().getId());
        return returnedReservationDto;

    }

    public ReservationDto getReservation(Integer id) {
        Optional<Reservation> foundReservation = reservationRepository.findById(id);
        if (foundReservation.isPresent()) {
            return ReservationMapper.MAPPER.toDto(foundReservation.get());
        } else throw new IdNotFoundException("Reservation Id not found.");
    }

    public void deleteReservation(Integer id) {
        if (!reservationRepository.existsById(id)) {
            throw new IdNotFoundException("Reservation Id not found.");
        }
        reservationRepository.deleteById(id);
    }
}
