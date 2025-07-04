package com.relatos.ms_books_catalogue.controllers.response;

import com.relatos.ms_books_catalogue.domains.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {

    private String firstName;
    private String lastName;

    public AuthorResponse(Author author) {
        this.firstName = author.getFirstName();
        this.lastName = author.getLastName();
    }
}
