package de.fuseki.service;

import de.fuseki.dtos.CreateOrderDto;
import de.fuseki.dtos.OrderDto;
import de.fuseki.entities.Book;
import de.fuseki.entities.Order;
import de.fuseki.entities.Person;
import de.fuseki.exceptions.DateNotValidException;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.mapper.OrderMapper;
import de.fuseki.repository.BookRepository;
import de.fuseki.repository.OrderRepository;
import de.fuseki.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    public void addOrder(CreateOrderDto createOrderDto) {
        if (createOrderDto.getPersonId() == null){
            throw new IsNullException("personId is null");
        }
        if (createOrderDto.getBookId() == null) {
            throw new IsNullException("bookId is null");
        }
        if(createOrderDto.getEndDate() != null &&
                createOrderDto.getBeginDate() != null &&
                createOrderDto.getBeginDate().isAfter(createOrderDto.getEndDate())){
            throw new DateNotValidException("Dates not valid");
        }
        Optional<Person> optional = personRepository.findById(createOrderDto.getPersonId());
        Person person;
        if (optional.isPresent()) {
            person = optional.get();
        }else throw new IdNotFoundException("personId not found");

        Optional<Book> optionalBook = bookRepository.findById(createOrderDto.getBookId());
        Book book;
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        }else throw new IsNullException("bookId not found");

        Order mappedOrder = orderMapper.toEntity(createOrderDto);
        mappedOrder.setBook(book);
        mappedOrder.setPerson(person);
        orderRepository.save(mappedOrder);
    }

}
