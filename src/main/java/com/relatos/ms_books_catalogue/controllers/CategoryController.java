package com.relatos.ms_books_catalogue.controllers;

import com.relatos.ms_books_catalogue.controllers.request.CreateCategoryRequest;
import com.relatos.ms_books_catalogue.services.CategoryService;
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
@RequestMapping("/categories")
@Tag(name = "Categorias", description = "Controlador para administrar las categorias")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Crear categorias", description = "Crea una categoria en el catalogo y retorna la categoria creada como respuesta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria creada"),
            @ApiResponse(responseCode = "500", description = "Error al crear la categoria")
    })
    public ResponseEntity<?> categories(@RequestBody @Valid CreateCategoryRequest categoryRequest) {
        try{
            return ResponseEntity.ok(categoryService.createCategories(categoryRequest));
        } catch(IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Consultar categoria", description = "Consulta las categoria por ID en el catalogo ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado de la categoria"),
            @ApiResponse(responseCode = "500", description = "Error al encontrar la categoria")
    })
    public ResponseEntity<?> categories(@PathVariable Long categoryId) {
        try{
            return ResponseEntity.ok(categoryService.getCategory(categoryId));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Consultar categorias", description = "Consulta las categorias en el catalogo paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado de las categorias"),
            @ApiResponse(responseCode = "500", description = "Error al encontrar las categorias")
    })
    public ResponseEntity<?> categories(@RequestParam int page,
                                        @RequestParam int size) {
        try{
            return ResponseEntity.ok(categoryService.getAllCategories(page, size));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Consultar categorias", description = "Consulta las categorias en el catalogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado de las categorias"),
            @ApiResponse(responseCode = "500", description = "Error al encontrar las categorias")
    })
    public ResponseEntity<?> categories() {
        try{
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Actualizar categoria", description = "Actualiza la categoria por ID en el catalogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna la categoria actualizada en la respuesta"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar la categoria")
    })
    public ResponseEntity<?> categories(@PathVariable Long categoryId, @RequestBody @Valid CreateCategoryRequest categoryRequest) {
        try{
            return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryRequest));
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Eliminar categoria", description = "Elimina la categoria por ID en el catalogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la categoria")
    })
    public ResponseEntity<?> deleteCategories(@PathVariable Long categoryId) {
        try{
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

}
