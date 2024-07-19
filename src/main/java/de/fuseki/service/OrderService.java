package de.fuseki.service;

import de.fuseki.mapper.MediaOrderMapper;
import de.fuseki.repository.BookRepository;
import de.fuseki.repository.MediaOrderRepository;
import de.fuseki.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MediaOrderRepository mediaOrderRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final MediaOrderMapper mediaOrderMapper;


}
