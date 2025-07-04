package com.relatos.ms_books_catalogue.domains;

import com.relatos.ms_books_catalogue.controllers.request.CreateAuthorRequest;
import com.relatos.ms_books_catalogue.domains.commons.SoftEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author extends SoftEntity {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    public Author(CreateAuthorRequest createAuthorRequest) {
        this.firstName = createAuthorRequest.getName();
        this.lastName = createAuthorRequest.getLastName();
    }

}
