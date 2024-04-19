package de.fuseki.controller;

import de.fuseki.dtos.BookDto;
import de.fuseki.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/book")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.addBook(bookDto));
    }

    @PutMapping("/book")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto){
        return ResponseEntity.ok(bookService.updateBook(bookDto));
    }
}
