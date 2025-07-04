package com.relatos.ms_books_catalogue.repositories;

import com.relatos.ms_books_catalogue.domains.Author;
import com.relatos.ms_books_catalogue.repositories.commons.SoftRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends SoftRepository<Author> {

    @Query("SELECT a FROM Author a WHERE LOWER(a.firstName) = LOWER(:firstName) AND LOWER(a.lastName) = LOWER(:lastName) AND a.deleted = false")
    Author findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT a FROM Author a " +
            "WHERE a.deleted = false " +
            "AND (COALESCE(:name, '') = '' OR LOWER(a.firstName) = LOWER(:name)) " +
            "AND (COALESCE(:lastName, '') = '' OR LOWER(a.lastName) = LOWER(:lastName)) " +
            "ORDER BY a.createdDate DESC")
    Page<Author> findAllByFilter(Pageable pageable, @Param("name") String name, @Param("lastName") String lastName);
}
