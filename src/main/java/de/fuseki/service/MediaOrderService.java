package de.fuseki.service;

import de.fuseki.dtos.CreateMediaOrderDto;
import de.fuseki.entities.Book;
import de.fuseki.entities.MediaOrder;
import de.fuseki.entities.Person;
import de.fuseki.exceptions.DateNotValidException;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.mapper.OrderMapper;
import de.fuseki.repository.BookRepository;
import de.fuseki.repository.MediaOrderRepository;
import de.fuseki.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaOrderService {

    private final MediaOrderRepository mediaOrderRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    public void addOrder(CreateMediaOrderDto createMediaOrderDto) {
        if (createMediaOrderDto.getPersonId() == null) {
            throw new IsNullException("personId is null");
        }
        if (createMediaOrderDto.getBookId() == null) {
            throw new IsNullException("bookId is null");
        }
        if (createMediaOrderDto.getEndDate() == null ||
                createMediaOrderDto.getBeginDate() == null ||
                createMediaOrderDto.getBeginDate().isAfter(createMediaOrderDto.getEndDate()) ||
                createMediaOrderDto.getEndDate().isBefore(LocalDate.now())) {
            throw new DateNotValidException("Dates not valid");
        }
        Optional<Person> optional = personRepository.findById(createMediaOrderDto.getPersonId());
        Person person;
        if (optional.isPresent()) {
            person = optional.get();
        } else throw new IdNotFoundException("personId not found.");

        Optional<Book> optionalBook = bookRepository.findById(createMediaOrderDto.getBookId());
        Book book;
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        } else throw new IsNullException("bookId not found");

        if (book.getBusyDate() != null && book.getBusyDate().isAfter(LocalDate.now())){
            throw new DateNotValidException("Book not available.");
        }

        MediaOrder mappedMediaOrder = orderMapper.toEntity(createMediaOrderDto);
        book.setBusyDate(mappedMediaOrder.getEndDate());
        person.getMediaOrderList().add(mappedMediaOrder);
        mappedMediaOrder.setBook(book);
        mappedMediaOrder.setPerson(person);
        mediaOrderRepository.save(mappedMediaOrder);
    }

}
