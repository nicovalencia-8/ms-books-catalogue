package com.relatos.ms_books_catalogue.controllers;

import com.relatos.ms_books_catalogue.controllers.request.CreateAuthorRequest;
import com.relatos.ms_books_catalogue.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/authors")
@Tag(name = "Autores", description = "Controlador para administrar los autores")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @Operation(summary = "Crear autores", description = "Crea un autor en el catalogo y retorna el autor creada como respuesta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor creado"),
            @ApiResponse(responseCode = "500", description = "Error al crear el autor")
    })
    public ResponseEntity<?> authors(@RequestBody @Valid CreateAuthorRequest authorRequest) {
        try{
            return ResponseEntity.ok(authorService.createAuthor(authorRequest));
        } catch(IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{authorId}")
    @Operation(summary = "Consultar autor", description = "Consulta un autor en el catalogo por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado del autor"),
            @ApiResponse(responseCode = "500", description = "Error al encontrar el autor")
    })
    public ResponseEntity<?> authors(@PathVariable Long authorId) {
        try{
            return ResponseEntity.ok(authorService.getAuthorById(authorId));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Consultar autor", description = "Consulta un autor en el catalogo por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado del autor"),
            @ApiResponse(responseCode = "500", description = "Error al encontrar el autor")
    })
    public ResponseEntity<?> authors(@RequestParam(required = false) String authorName,
                                     @RequestParam(required = false) String authorLastName,
                                     @RequestParam int page,
                                     @RequestParam int size) {
        try{
            return ResponseEntity.ok(authorService.getAllAuthorsByFilter(authorName, authorLastName, page, size));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/{authorId}")
    @Operation(summary = "Actualizar autor", description = "Actualiza el autor por ID en el catalogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna el autor actualizado en la respuesta"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el autor")
    })
    public ResponseEntity<?> authors(@PathVariable Long authorId, @RequestBody @Valid CreateAuthorRequest authorRequest) {
        try{
            return ResponseEntity.ok(authorService.updateAuthor(authorId, authorRequest));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{authorId}")
    @Operation(summary = "Eliminar autor", description = "Elimina el autor por ID en el catalogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor eliminado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el autor")
    })
    public ResponseEntity<?> deleteAuthors(@PathVariable Long authorId) {
        try{
            authorService.deleteAuthor(authorId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

}
