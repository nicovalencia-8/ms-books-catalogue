package com.relatos.ms_books_catalogue.controllers.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateBookRequest {

    @NotBlank(message = "Title can not be empty")
    private String title;

    @NotBlank(message = "Description can not be empty")
    private String description;

    @NotBlank(message = "ISBN can not be empty")
    private String ISBN;

    @NotNull
    private LocalDateTime publishedDate;

    @NotNull
    private Integer stock;

    @NotNull
    private Double price;

    private Double rating;

    @Valid
    private Long author;

    @NotBlank(message = "Image Date can not be empty")
    private String urlImage;

    @Valid
    private List<Long> category;

    @NotNull
    private Boolean visibility;

}
