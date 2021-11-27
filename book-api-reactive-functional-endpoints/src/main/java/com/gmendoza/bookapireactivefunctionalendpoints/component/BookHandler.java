package com.gmendoza.bookapireactivefunctionalendpoints.component;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import com.gmendoza.bookapireactivefunctionalendpoints.model.Book;
import com.gmendoza.bookapireactivefunctionalendpoints.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BookHandler {

    private BookRepository bookRepository;

    public BookHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<ServerResponse> findAllBooks(ServerRequest serverRequest) {
        Flux<Book> books = this.bookRepository.findAll();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(books, Book.class);
    }

    public Mono<ServerResponse> findBookById(ServerRequest serverRequest) {
        String bookId = serverRequest.pathVariable("id");
        Mono<Book> bookMono = this.bookRepository.findById(bookId);
        return bookMono.flatMap(book -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(book)))
                .switchIfEmpty(notFound());
    }

    public Mono<ServerResponse> createBook(ServerRequest serverRequest) {
        Mono<Book> bookMono = serverRequest.bodyToMono(Book.class);

        return bookMono.flatMap(book -> ServerResponse.status(HttpStatus.CREATED).contentType(APPLICATION_JSON)
                .body(this.bookRepository.save(book), Book.class));
    }

    public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
        String bookId = serverRequest.pathVariable("id");
        Mono<Book> existingBookMono = this.bookRepository.findById(bookId);
        Mono<Book> newBookMono = serverRequest.bodyToMono(Book.class);
        return newBookMono
                .zipWith(existingBookMono,
                        (newBook, existingBook) -> Book.builder().id(existingBook.getId())
                                .name(newBook.getName()).category(newBook.getCategory())
                                .rating(newBook.getRating()).description(newBook.getDescription()).build())
                .flatMap(book -> ServerResponse.ok().contentType(APPLICATION_JSON)
                        .body(this.bookRepository.save(book), Book.class))
                .switchIfEmpty(notFound());
    }

    public Mono<ServerResponse> deleteBook(ServerRequest serverRequest) {
        String bookId = serverRequest.pathVariable("id");
        return this.bookRepository.findById(bookId)
                .flatMap(existingBook -> ServerResponse.ok().build(this.bookRepository.deleteById(bookId)))
                .switchIfEmpty(notFound());
    }

    public Mono<ServerResponse> deleteAllBooks(ServerRequest serverRequest) {
        return ServerResponse.ok().build(this.bookRepository.deleteAll());
    }

    private Mono<ServerResponse> notFound() {
        return ServerResponse.notFound().build();
    }
}
