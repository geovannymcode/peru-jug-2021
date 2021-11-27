package com.gmendoza.bookapicontroller.controller;

import com.gmendoza.bookapicontroller.model.Book;
import com.gmendoza.bookapicontroller.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/books/")
public class BookController {

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Book>> getBookById(@PathVariable("id") String bookId) {
        return bookRepository.findById(bookId)
                .map(book -> ResponseEntity.ok(book))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("category/{name}")
    public Flux<Book> getBookByCategory(@PathVariable("name") String category) {
        return bookRepository.findAllByCategory(category)
                .doOnError(e -> log.error("Failed to create book", e.getMessage()));
    }

    @PostMapping
    public Mono<Book> createBook(@RequestBody Book book) {
        return bookRepository.save(book)
                .doOnSuccess(updatedBook -> log.info("Successfully created book", updatedBook))
                .doOnError(e -> log.error("Failed to create book", e.getMessage()));
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Book>> updateBook(@PathVariable("id") String bookId, @RequestBody Book book) {

        return this.bookRepository.findById(bookId).flatMap(existingBook -> {
                    existingBook.setName(book.getName());
                    existingBook.setRating(book.getRating());
                    existingBook.setCategory(book.getCategory());
                    existingBook.setDescription(book.getDescription());
                    return this.bookRepository.save(existingBook);
                }).map(updatedBook -> ResponseEntity.ok(updatedBook)).defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(e -> log.error("Failed to update book", e.getMessage()));

    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Book>> deleteBookById(@PathVariable("id") String bookId) {
        return this.bookRepository.findById(bookId).flatMap(
                        course -> this.bookRepository.deleteById(course.getId()).then(Mono.just(ResponseEntity.ok(course))))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteBooks() {
        return bookRepository.deleteAll();
    }

}
