package com.gmendoza.bookapireactivefunctionalendpoints.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.gmendoza.bookapireactivefunctionalendpoints.component.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterContext {


    @Bean
    RouterFunction<ServerResponse> routes(BookHandler bookHandler) {
        return route(GET("/books").and(accept(APPLICATION_JSON)), bookHandler::findAllBooks)
                .andRoute(GET("/books/{id}").and(accept(APPLICATION_JSON)), bookHandler::findBookById)
                .andRoute(POST("/books").and(accept(APPLICATION_JSON)), bookHandler::createBook)
                .andRoute(PUT("/books").and(accept(APPLICATION_JSON)), bookHandler::updateBook)
                .andRoute(DELETE("/books/{id}").and(accept(APPLICATION_JSON)), bookHandler::deleteBook)
                .andRoute(DELETE("/books").and(accept(APPLICATION_JSON)), bookHandler::deleteAllBooks);
    }
}
