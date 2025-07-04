package com.relatos.ms_books_catalogue.domains;

import com.relatos.ms_books_catalogue.controllers.request.CreateCategoryRequest;
import com.relatos.ms_books_catalogue.domains.commons.SoftEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "category_name", name = "category_Name"))
public class Category extends SoftEntity {

    @NotNull
    private String categoryName;

    public Category(CreateCategoryRequest categoryRequest) {
        this.categoryName = categoryRequest.getName();
    }

}
