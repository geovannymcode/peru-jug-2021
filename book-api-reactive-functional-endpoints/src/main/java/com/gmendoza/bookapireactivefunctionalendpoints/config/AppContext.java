package com.gmendoza.bookapireactivefunctionalendpoints.config;

import com.gmendoza.bookapireactivefunctionalendpoints.model.Book;
import com.gmendoza.bookapireactivefunctionalendpoints.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class AppContext {

    @Bean
    public CommandLineRunner init(BookRepository bookRepository) {
        return args -> {

            Book book1 = Book.builder().name("\n" +
                            "Building Modern Web Applications With JakartaEE").category("JakartaEE").rating(5)
                    .description("Build Modern Web Apps with JakartaEE, Jmoordb, and Vaadins").build();
            Book book2 = Book.builder().name("Practical Vaadin").category("Vaadin").rating(4)
                    .description("Practical Vaadin: Developing Web Applications in Java").build();
            Book book3 = Book.builder().name("Spring Boot in Practice").category("Spring").rating(3)
                    .description("Spring Boot in Practice")
                    .build();

            Flux.just(book1, book2, book3).flatMap(bookRepository::save).thenMany(bookRepository.findAll())
                    .subscribe(System.out::println);
        };
    }
}
