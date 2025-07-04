package com.relatos.ms_books_catalogue.services;

import com.relatos.ms_books_catalogue.controllers.request.CreateAuthorRequest;
import com.relatos.ms_books_catalogue.controllers.response.AuthorResponse;
import com.relatos.ms_books_catalogue.controllers.response.commons.PageResponse;
import com.relatos.ms_books_catalogue.domains.Author;
import com.relatos.ms_books_catalogue.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author createAuthor(CreateAuthorRequest authorRequest) {
        Author author = authorRepository.findByFirstNameAndLastName(authorRequest.getName(), authorRequest.getLastName());
        if (author == null) {
            author = new Author(authorRequest);
            return authorRepository.save(author);
        } else {
            throw new IllegalArgumentException("El autor ya se encuentra registrado");
        }
    }

    public Author getAuthorById(Long Id) {
        Author author = authorRepository.findByIdC(Id);
        if (author != null) {
            return author;
        } else {
            throw new IllegalArgumentException("El autor no se encuentra registrado");
        }
    }

    public PageResponse<AuthorResponse> getAllAuthorsByFilter(String authorName,
                                                              String authorLastName,
                                                              int page,
                                                              int size) {
        PageRequest requestPage = PageRequest.of(page, size);
        Page<Author> pageAuthor = authorRepository.findAllByFilter(requestPage, authorName, authorLastName);
        return new PageResponse<>(pageAuthor.map(AuthorResponse::new));
    }

    public Author updateAuthor(Long authorId, CreateAuthorRequest authorRequest) {
        Author author = authorRepository.findByIdC(authorId);
        if (author != null) {
            author.setFirstName(authorRequest.getName());
            author.setLastName(authorRequest.getLastName());
            return authorRepository.save(author);
        } else {
            throw new IllegalArgumentException("El autor no se encuentra registrado");
        }
    }

    public void deleteAuthor(Long authorId) {
        Author author = authorRepository.findByIdC(authorId);
        if (author != null){
            authorRepository.softDelete(authorId);
        } else {
            throw new IllegalArgumentException("El autor no se encuentra registrado");
        }
    }

}
