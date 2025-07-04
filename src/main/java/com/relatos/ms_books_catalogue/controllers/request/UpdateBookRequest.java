package com.relatos.ms_books_catalogue.controllers.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequest {

    private String title;
    private String description;
    private String ISBN;
    private LocalDateTime publishedDate;
    private Integer stock;
    private Double price;
    private Double rating;
    private Long author;
    private String urlImage;
    private List<Long> category;
    private Boolean visibility;

}
