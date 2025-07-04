package com.relatos.ms_books_catalogue.controllers.response;

import com.relatos.ms_books_catalogue.domains.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Integer id;
    private String title;
    private String description;
    private LocalDateTime publishedDate;
    private Integer stock;
    private Double price;
    private Double rating;
    private AuthorResponse author;
    private String image;
    private List<CategoryResponse> category;
    private String isbn;

    public BookResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.publishedDate = book.getPublishedDate();
        this.stock = book.getStock();
        this.price = book.getPrice();
        this.rating = book.getRating();
        this.author = new AuthorResponse(book.getAuthor());
        this.image = book.getImage();
        this.category = book.getCategory().stream().map(CategoryResponse::new).collect(Collectors.toList());
        this.isbn =  book.getIsbn();
    }

}
