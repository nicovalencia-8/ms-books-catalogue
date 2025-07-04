package com.relatos.ms_books_catalogue.repositories;

import com.relatos.ms_books_catalogue.domains.Book;
import com.relatos.ms_books_catalogue.repositories.commons.SoftRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookRepository extends SoftRepository<Book> {

    @Query("SELECT b FROM Book b WHERE b.deleted = false AND LOWER(b.ISBN) = LOWER(:isbn)")
    Book findByISBN(@Param("isbn") String isbn);

    @Query("SELECT b FROM Book b " +
            "WHERE b.deleted = false " +
            "AND (COALESCE(:title, '') = '' OR LOWER(b.title) = LOWER(:title)) " +
            "AND (:author IS NULL OR b.author.id = :author) " +
            "AND (COALESCE(:publishedDate, '1900-01-01T00:00:00') = '1900-01-01T00:00:00' OR b.publishedDate = :publishedDate)" +
            "AND (COALESCE(:category, 0) = 0 OR :category IN (SELECT c.id FROM b.categories c)) " +
            "AND (COALESCE(:isbn, '') = '' OR LOWER(b.ISBN) = LOWER(:isbn)) " +
            "AND (:rating IS NULL OR b.rating = :rating) " +
            "AND (:visibility IS NULL OR b.visibility = :visibility) ")
    Page<Book> findAllByFilters(Pageable pageable,
                             @Param("title") String title,
                             @Param("author") Long author,
                             @Param("publishedDate") LocalDateTime publishedDate,
                             @Param("category") Long category,
                             @Param("isbn") String isbn,
                             @Param("rating") Double rating,
                             @Param("visibility") Boolean visibility);
}
