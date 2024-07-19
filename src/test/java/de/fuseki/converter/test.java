package de.fuseki.converter;


import de.fuseki.entities.Book;
import de.fuseki.entities.Person;
import de.fuseki.entities.Reservation;
import de.fuseki.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;

public class test {
    @Test
    public void test() {
        Person testperson = Person.builder().id(1).build();
        Book testbook = Book.builder().id(1).build();
        Reservation testreservation = Reservation.builder().id(1).person(testperson).book(testbook).build();
        System.out.println(ReservationMapper.MAPPER.toDto(testreservation).getPersonId());

    }
}
