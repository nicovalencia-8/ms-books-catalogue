package com.relatos.ms_books_catalogue.domains;

import com.relatos.ms_books_catalogue.controllers.request.CreateBookRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "books", createIndex = true)
public class Book {

    @Id
    private Integer id;

    @Field(type = FieldType.Search_As_You_Type)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime publishedDate;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Float)
    private Double rating;

    @Field(type = FieldType.Object)
    private Author author;

    @Field(type = FieldType.Keyword)
    private String image;

    @Field(type = FieldType.Nested)
    private List<Category> category;

    @Field(type = FieldType.Boolean)
    private Boolean visibility;

    public Book(CreateBookRequest bookRequest, Author author, List<Category> category) {
        this.title = bookRequest.getTitle();
        this.description = bookRequest.getDescription();
        this.isbn = bookRequest.getISBN();
        this.publishedDate = bookRequest.getPublishedDate();
        this.stock = bookRequest.getStock();
        this.price = bookRequest.getPrice();
        this.rating = bookRequest.getRating();
        this.author = author;
        this.image = bookRequest.getUrlImage();
        this.category = category;
        this.visibility = bookRequest.getVisibility();
    }

}
