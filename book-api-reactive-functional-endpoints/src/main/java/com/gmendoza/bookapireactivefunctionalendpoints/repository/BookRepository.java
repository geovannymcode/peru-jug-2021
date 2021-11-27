package com.gmendoza.bookapireactivefunctionalendpoints.repository;

import com.gmendoza.bookapireactivefunctionalendpoints.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Flux<Book> findAllByCategory(String category);
}
