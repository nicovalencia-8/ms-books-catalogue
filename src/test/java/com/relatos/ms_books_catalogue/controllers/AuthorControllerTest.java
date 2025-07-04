package com.relatos.ms_books_catalogue.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relatos.ms_books_catalogue.controllers.request.CreateAuthorRequest;
import com.relatos.ms_books_catalogue.controllers.response.AuthorResponse;
import com.relatos.ms_books_catalogue.controllers.response.commons.PageResponse;
import com.relatos.ms_books_catalogue.domains.Author;
import com.relatos.ms_books_catalogue.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.function.Function;


@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    private MockMvc mockMvc;
    private final AuthorService authorService = Mockito.mock(AuthorService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        AuthorController authorController = new AuthorController(authorService);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    private final Function<String, String> getPathBase = path -> "/authors" + path;


    @Test
    void authorsTest() throws Exception {

        Author author = new Author();
        author.setFirstName("Nicolas");
        author.setLastName("Valencia");

        Mockito.when(
                authorService.createAuthor(ArgumentMatchers.any(CreateAuthorRequest.class))
        ).thenReturn(author);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(getPathBase.apply(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                       new CreateAuthorRequest("Nicolas", "Valencia")
                                )
                        ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void authorsWhenThrowsException() throws Exception {
        Mockito.when(
                authorService.createAuthor(ArgumentMatchers.any(CreateAuthorRequest.class))
        ).thenThrow(new IllegalArgumentException("Error al crear el autor"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.post(getPathBase.apply(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        new CreateAuthorRequest("Nicolas", "Valencia")
                                )
                        ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Error al crear el autor"));
    }

    @Test
    void authors() throws Exception {
        Mockito.when(authorService.getAuthorById(1L)).thenReturn(new Author("Nicolas", "Valencia"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get(getPathBase.apply("/1")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Nicolas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Valencia"));
    }

    @Test
    void authorWhenGetByIdThrowsException() throws Exception {
        Mockito.when(authorService.getAuthorById(1L))
                .thenThrow(new IllegalArgumentException("Error al encontrar el autor"));

        mockMvc.perform(MockMvcRequestBuilders.get(getPathBase.apply("/1")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Error al encontrar el autor"));

    }

    @Test
    void authorsAll() throws Exception {
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setFirstName("Nicolas");
        authorResponse.setLastName("Valencia");


        PageResponse<AuthorResponse> pageResponse = new PageResponse<>(
                new PageImpl<>(List.of(authorResponse))
        );

        Mockito.when(authorService.getAllAuthorsByFilter("Nicolas", "Valencia", 0, 10))
                .thenReturn(pageResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get(getPathBase.apply(""))
                        .param("authorName", "Nicolas")
                        .param("authorLastName", "Valencia")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Nicolas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Valencia"));
    }

    @Test
    void authorAllWhenGetByIdThrowsException() throws Exception {

        Mockito.when(authorService.getAllAuthorsByFilter(null, null, 0, 10))
                .thenThrow(new IllegalArgumentException("Error al encontrar el autor"));

        mockMvc.perform(MockMvcRequestBuilders.get(getPathBase.apply(""))
                    .param("page", "0")
                    .param("size", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Error al encontrar el autor"));

    }

    @Test
    void authorUpdateTest() throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();

        createAuthorRequest.setName("back");
        createAuthorRequest.setLastName("Update");


        Author updatedAuthor = new Author("Nicolas", "Valencia update");

        Mockito.when(authorService.updateAuthor(Mockito.eq(1L), Mockito.any(CreateAuthorRequest.class)))
                .thenReturn(updatedAuthor);

        this.mockMvc.perform(MockMvcRequestBuilders.put(getPathBase.apply("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Nicolas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Valencia update"));
    }


    @Test
    void authorUpdateTestThrowsException() throws Exception {
        CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();

        createAuthorRequest.setName("Nicolas");
        createAuthorRequest.setLastName("Valencia update");


        Mockito.when(authorService.updateAuthor(Mockito.eq(1L), Mockito.any(CreateAuthorRequest.class)))
                .thenThrow(new IllegalArgumentException("Error al actualizar el autor"));

        this.mockMvc.perform(MockMvcRequestBuilders.put(getPathBase.apply("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAuthorRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Error al actualizar el autor"));
    }


    @Test
    void deleteAuthorTest() throws Exception {
        Mockito.doNothing().when(authorService).deleteAuthor(1L);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(getPathBase.apply("/1")))


                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteAuthorTestThrowsException() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Error al eliminar el autor"))
                .when(authorService).deleteAuthor(1L);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(getPathBase.apply("/1")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Error al eliminar el autor"));
    }


}