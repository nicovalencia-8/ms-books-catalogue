package com.relatos.ms_books_catalogue.repositories;

import com.relatos.ms_books_catalogue.domains.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, Integer> {
    Optional<Book> findByisbnIgnoreCase(String isbn);
}