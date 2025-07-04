package com.relatos.ms_books_catalogue.domains;

import com.relatos.ms_books_catalogue.controllers.request.CreateAuthorRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    private String firstName;
    private String lastName;

    public Author(CreateAuthorRequest authorRequest) {
        this.firstName = authorRequest.getName();
        this.lastName = authorRequest.getLastName();
    }
}
