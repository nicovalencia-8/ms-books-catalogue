package com.relatos.ms_books_catalogue.services;

import com.relatos.ms_books_catalogue.controllers.request.CreateBookRequest;
import com.relatos.ms_books_catalogue.controllers.request.UpdateBookRequest;
import com.relatos.ms_books_catalogue.controllers.response.BookResponse;
import com.relatos.ms_books_catalogue.controllers.response.commons.GeneralResponse;
import com.relatos.ms_books_catalogue.controllers.response.commons.PageResponse;
import com.relatos.ms_books_catalogue.domains.Author;
import com.relatos.ms_books_catalogue.domains.Book;
import com.relatos.ms_books_catalogue.domains.Category;
import com.relatos.ms_books_catalogue.domains.Image;
import com.relatos.ms_books_catalogue.repositories.AuthorRepository;
import com.relatos.ms_books_catalogue.repositories.BookRepository;
import com.relatos.ms_books_catalogue.repositories.CategoryRepository;
import com.relatos.ms_books_catalogue.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BookService {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public GeneralResponse<BookResponse> createBook(CreateBookRequest bookRequest) {
        Book book = bookRepository.findByISBN(bookRequest.getISBN());
        if (book == null) {

            Author author = validateAuthor(bookRequest.getAuthor());
            List<Category> categories = validateCategory(bookRequest.getCategory());

            Image image = new Image();
            image.setUrlImage(bookRequest.getUrlImage());
            image = imageRepository.save(image);
            book = new Book(bookRequest, image, author, categories);
            book = bookRepository.save(book);
            return new GeneralResponse<>(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    "Libro creado",
                    new BookResponse(book)
            );
        } else {
            throw new IllegalArgumentException("El libro ya se encuentra registrado");
        }
    }

    public GeneralResponse<BookResponse> getBookById(@Param("id") Long id) {
        Book book = bookRepository.findByIdC(id);
        if (book != null){
            return new GeneralResponse<>(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    "Libro creado",
                    new BookResponse(book)
            );
        } else {
            throw new IllegalArgumentException("Libro no encontrado");
        }
    }

    public GeneralResponse<PageResponse<BookResponse>> getAllBooksByFilter(String title,
                                                          Long authorId,
                                                          LocalDateTime publishedDate,
                                                          Long categoryId,
                                                          String isbn,
                                                          Double rating,
                                                          Boolean visibility,
                                                          int page,
                                                          int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> pageBook = bookRepository.findAllByFilters(pageRequest, title, authorId, publishedDate, categoryId, isbn, rating, visibility);
        return new GeneralResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Resultado del libro",
                new PageResponse<>(pageBook.map(BookResponse::new))
        );
    }

    @Transactional
    public GeneralResponse<BookResponse> updateBookById(@Param("id") Long bookId, CreateBookRequest bookRequest) {
        Book book = bookRepository.findByIdC(bookId);
        if (book == null){
            throw new IllegalArgumentException("Libro no encontrado");
        }
        Author author = validateAuthor(bookRequest.getAuthor());
        List<Category> categories = validateCategory(bookRequest.getCategory());

        Image image = imageRepository.findByIdC(book.getImage().getId());
        image.setUrlImage(bookRequest.getUrlImage());
        image = imageRepository.save(image);
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setISBN(bookRequest.getISBN());
        book.setPublishedDate(bookRequest.getPublishedDate());
        book.setStock(bookRequest.getStock());
        book.setPrice(bookRequest.getPrice());
        book.setRating(bookRequest.getRating());
        book.setAuthor(author);
        book.setImage(image);
        book.setCategories(categories);
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
    public GeneralResponse<BookResponse> updateBookById(@Param("id") Long bookId, UpdateBookRequest bookRequest) {
        Book book = bookRepository.findByIdC(bookId);
        if (book == null){
            throw new IllegalArgumentException("Libro no encontrado");
        }
        if(bookRequest.getAuthor() != null) {
            Author author = validateAuthor(bookRequest.getAuthor());
            book.setAuthor(author);
        }
        if(bookRequest.getCategory() != null) {
            if (bookRequest.getCategory().isEmpty()){
                book.setCategories(new ArrayList<>());
            } else {
                List<Category> categories = validateCategory(bookRequest.getCategory());
                if(categories.size() != bookRequest.getCategory().size()){
                    throw new IllegalArgumentException("Al menos una de las categorias no existe");
                }
                book.setCategories(categories);
            }
        }
        if(bookRequest.getUrlImage() != null) {
            Image image = imageRepository.findByIdC(book.getImage().getId());
            if (image == null) {
                throw new IllegalArgumentException("No se encontro la imagen a actualizar");
            }
            image.setUrlImage(bookRequest.getUrlImage());
            image = imageRepository.save(image);
            book.setImage(image);
        }
        if(bookRequest.getTitle() != null) book.setTitle(bookRequest.getTitle());
        if(bookRequest.getDescription() != null) book.setDescription(bookRequest.getDescription());
        if(bookRequest.getISBN() != null) book.setISBN(bookRequest.getISBN());
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
    public GeneralResponse<?> deleteBook(Long bookId) {
        Book book = bookRepository.findByIdC(bookId);
        if (book != null){
            bookRepository.softDelete(bookId);

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

    private Author validateAuthor(Long authorId) {
        Author author = authorRepository.findByIdC(authorId);
        if (author == null) {
            throw new IllegalArgumentException("El autor no existe");
        }
        return author;
    }

    private List<Category> validateCategory(List<Long> categoryId) {
        List<Category> categories = categoryRepository.findAllByIds(categoryId);
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("La categoria no existe");
        }
        return categories;
    }

}
