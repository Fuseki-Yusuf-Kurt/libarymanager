package de.fuseki.service;

import de.fuseki.repository.BookRepository;
import de.fuseki.repository.MediaOrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@Transactional
class MediaOrderServiceTest {
    @Mock
    MediaOrderRepository mediaOrderRepository;
    @InjectMocks
    MediaOrderService underTest;


    @BeforeEach
    void setUp() {
    }

    @Test
    void addOrder() {
    }
}