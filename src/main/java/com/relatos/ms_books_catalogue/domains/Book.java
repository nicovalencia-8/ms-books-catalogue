package com.relatos.ms_books_catalogue.domains;

import com.relatos.ms_books_catalogue.controllers.request.CreateBookRequest;
import com.relatos.ms_books_catalogue.domains.commons.SoftEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "ISBN", name = "books_ISBN"))
public class Book extends SoftEntity {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String ISBN;

    @NotNull
    private LocalDateTime publishedDate;

    @NotNull
    private Integer stock;

    @NotNull
    private Double price;

    private Double rating;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    private Boolean visibility;

    public Book(CreateBookRequest bookRequest, Image image, Author author, List<Category> categories) {
        this.title = bookRequest.getTitle();
        this.description = bookRequest.getDescription();
        this.ISBN = bookRequest.getISBN();
        this.publishedDate = bookRequest.getPublishedDate();
        this.stock = bookRequest.getStock();
        this.price = bookRequest.getPrice();
        this.rating = bookRequest.getRating();
        this.author = author;
        this.image = image;
        this.categories = categories;
        this.visibility = bookRequest.getVisibility();
    }

}
