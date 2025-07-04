package com.relatos.ms_books_catalogue.controllers.response;

import com.relatos.ms_books_catalogue.domains.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private String categoryName;

    public CategoryResponse(Category category) {
        this.categoryName = category.getCategoryName();
    }
}
