package de.fuseki.service;

import de.fuseki.dtos.CreateMediaOrderDto;
import de.fuseki.entities.Book;
import de.fuseki.entities.MediaOrder;
import de.fuseki.entities.Person;
import de.fuseki.exceptions.DateNotValidException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.mapper.MediaOrderMapper;
import de.fuseki.repository.MediaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MediaOrderService {
// Enddatum der Leihe festlegen
    // Reservierung Klasse und Table erstellen
    private final MediaOrderRepository mediaOrderRepository;
    private final PersonService personService;
    private final BookService bookService;
    private final MediaOrderMapper mediaOrderMapper;

    public void addOrder(CreateMediaOrderDto mediaOrderDto) {
        if (mediaOrderDto.getPersonId() == null) {
            throw new IsNullException("personId is null");
        }
        if (mediaOrderDto.getBookId() == null) {
            throw new IsNullException("bookId is null");
        }
        if (mediaOrderDto.getEndDate() == null ||
                mediaOrderDto.getBeginDate() == null ||
                mediaOrderDto.getBeginDate().isAfter(mediaOrderDto.getEndDate()) ||
                mediaOrderDto.getEndDate().isBefore(LocalDate.now())) {
            throw new DateNotValidException("Dates not valid");
        }

        Person person = personService.getPersonFromDatabase(mediaOrderDto.getPersonId());
        Book book = bookService.getBookFromDatabase(mediaOrderDto.getBookId());

        if (book.getLendDate() != null){
            throw new DateNotValidException("Book not available.");
        }

        MediaOrder mappedMediaOrder = mediaOrderMapper.toEntity(mediaOrderDto);
        book.setLendDate(mappedMediaOrder.getEndDate());
        person.getMediaOrderList().add(mappedMediaOrder);
        mappedMediaOrder.setBook(book);
        mappedMediaOrder.setPerson(person);
        mediaOrderRepository.save(mappedMediaOrder);
    }

}
