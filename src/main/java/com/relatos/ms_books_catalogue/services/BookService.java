package com.relatos.ms_books_catalogue.services;

import com.relatos.ms_books_catalogue.controllers.request.CreateBookRequest;
import com.relatos.ms_books_catalogue.controllers.request.UpdateBookRequest;
import com.relatos.ms_books_catalogue.controllers.response.BookResponse;
import com.relatos.ms_books_catalogue.controllers.response.commons.GeneralResponse;
import com.relatos.ms_books_catalogue.domains.Author;
import com.relatos.ms_books_catalogue.domains.Book;
import com.relatos.ms_books_catalogue.domains.Category;
import com.relatos.ms_books_catalogue.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookDataAccessRepository bookDataAccessRepository;

    @Transactional
    public GeneralResponse<BookResponse> createBook(CreateBookRequest bookRequest) {
        Optional<Book> existingBook = bookRepository.findByisbnIgnoreCase(bookRequest.getISBN());

        if (existingBook.isPresent()) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + bookRequest.getISBN());
        }

        Author author = new Author(bookRequest.getAuthor());
        List<Category> categories = bookRequest.getCategory().stream()
                    .map(req -> new Category(req.getName()))
                    .toList();

        Book book = new Book(bookRequest, author, categories);
        book = bookDataAccessRepository.save(book);
        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Libro creado",
                new BookResponse(book)
        );
    }

    public GeneralResponse<BookResponse> getBookById(@Param("id") Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Libro creado",
                new BookResponse(book)
        );

    }

    public GeneralResponse<List<BookResponse>> getAllBooksByFilter(String title,
                                                          String authorName,
                                                          String authorLastName,
                                                          LocalDateTime publishedDate,
                                                          String categoryName,
                                                          String isbn,
                                                          Double rating,
                                                          Boolean visibility,
                                                          int page,
                                                          int size) {
        List<BookResponse> pageBook = bookDataAccessRepository.findBooks(title, isbn, authorName, authorLastName, categoryName, publishedDate, visibility, rating, page, size);
        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Resultado del libro",
                pageBook
        );
    }

    @Transactional
    public GeneralResponse<BookResponse> updateBookById(@Param("id") Integer bookId, CreateBookRequest bookRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        Author author = new Author(bookRequest.getAuthor());
        List<Category> categories = bookRequest.getCategory().stream()
                .map(req -> new Category(req.getName()))
                .toList();

        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setIsbn(bookRequest.getISBN());
        book.setPublishedDate(bookRequest.getPublishedDate());
        book.setStock(bookRequest.getStock());
        book.setPrice(bookRequest.getPrice());
        book.setRating(bookRequest.getRating());
        book.setAuthor(author);
        book.setImage(bookRequest.getUrlImage());
        book.setCategory(categories);
        book.setVisibility(bookRequest.getVisibility());
        book = bookRepository.save(book);

        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Libro actualizado correctamente",
                new BookResponse(book)
        );
    }

    @Transactional
    public GeneralResponse<BookResponse> updateBookById(@Param("id") Integer bookId, UpdateBookRequest bookRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        if(bookRequest.getAuthor() != null) {
            Author author = new Author(bookRequest.getAuthor());
            book.setAuthor(author);
        }
        if(bookRequest.getCategory() != null) {
            if (bookRequest.getCategory().isEmpty()){
                book.setCategory(new ArrayList<>());
            } else {
                List<Category> categories = bookRequest.getCategory().stream()
                        .map(req -> new Category(req.getName()))
                        .toList();
                book.setCategory(categories);
            }
        }
        if(bookRequest.getUrlImage() != null) {
            book.setImage(bookRequest.getUrlImage());
        }
        if(bookRequest.getTitle() != null) book.setTitle(bookRequest.getTitle());
        if(bookRequest.getDescription() != null) book.setDescription(bookRequest.getDescription());
        if(bookRequest.getISBN() != null) book.setIsbn(bookRequest.getISBN());
        if(bookRequest.getPublishedDate() != null) book.setPublishedDate(bookRequest.getPublishedDate());
        if(bookRequest.getStock() != null) book.setStock(bookRequest.getStock());
        if(bookRequest.getPrice() != null) book.setPrice(bookRequest.getPrice());
        if(bookRequest.getRating() != null) book.setRating(bookRequest.getRating());
        if(bookRequest.getVisibility() != null) book.setVisibility(bookRequest.getVisibility());
        book = bookRepository.save(book);

        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Libro actualizado correctamente",
                new BookResponse(book)
        );
    }

    @Transactional
    public GeneralResponse<?> deleteBook(Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        if (book != null){
            bookRepository.deleteById(bookId);

            return new GeneralResponse<>(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    "Libro eliminado correctamente",
                    null
            );

        } else {
            throw new IllegalArgumentException("Libro no encontrado");
        }
    }

}
