package com.relatos.ms_books_catalogue.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.relatos.ms_books_catalogue.controllers.request.CreateAuthorRequest;
import com.relatos.ms_books_catalogue.controllers.request.CreateBookRequest;
import com.relatos.ms_books_catalogue.controllers.response.commons.GeneralResponse;
import com.relatos.ms_books_catalogue.domains.Author;
import com.relatos.ms_books_catalogue.services.AuthorService;
import com.relatos.ms_books_catalogue.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
public class BooksControllerTest {

    private MockMvc mockMvc;
    private final BookService bookService = Mockito.mock(BookService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        BooksController booksController = new BooksController(bookService);
        mockMvc = MockMvcBuilders.standaloneSetup(booksController).build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    private final Function<String, String> getPathBase = path -> "/books" + path;

    @Test
    void books() throws Exception {

        var request = getCreateBookRequest();
        Mockito.when(
                bookService.createBook(ArgumentMatchers.any())
        ).thenReturn(new GeneralResponse<>());

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(getPathBase.apply(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        request
                                )
                        ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void booksWhenThrowsException() throws Exception {

        var request = getCreateBookRequest();
        Mockito.when(
                bookService.createBook(ArgumentMatchers.any())
        ).thenThrow(new IllegalArgumentException("Error al crear el libro"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(getPathBase.apply(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        request
                                )
                        ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



    private static CreateBookRequest getCreateBookRequest() {
        var request = new CreateBookRequest();
        request.setTitle("Titulo Book");
        request.setDescription("Description book");
        request.setISBN("123ABC");
        request.setPublishedDate(LocalDateTime.of(1994, 11, 25, 0, 0));
        request.setStock(5);
        request.setPrice(1500.0);
        request.setRating(2.0);
        request.setAuthor(10L);
        request.setUrlImage("http://example.com/image.jpg");
        request.setCategory(List.of(5L));
        request.setVisibility(true);
        return request;
    }
}
