package com.gmendoza.bookapicontroller.repository;

import com.gmendoza.bookapicontroller.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Flux<Book> findAllByCategory(String category);
}
