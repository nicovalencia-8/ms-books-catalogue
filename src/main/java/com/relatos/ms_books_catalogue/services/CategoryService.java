package com.relatos.ms_books_catalogue.services;

import com.relatos.ms_books_catalogue.controllers.request.CreateCategoryRequest;
import com.relatos.ms_books_catalogue.controllers.response.CategoryResponse;
import com.relatos.ms_books_catalogue.controllers.response.commons.PageResponse;
import com.relatos.ms_books_catalogue.domains.Category;
import com.relatos.ms_books_catalogue.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategories(CreateCategoryRequest categoryRequest) {
        Category category = categoryRepository.findByCategoryName(categoryRequest.getName());
        if (category == null) {
            category = new Category(categoryRequest);
            return categoryRepository.save(category);
        } else {
            throw new IllegalArgumentException("La categoria ya existe");
        }
    }

    public Category getCategory(Long id) {
        Category category = categoryRepository.findByIdC(id);
        if (category != null) {
            return category;
        } else {
            throw new IllegalArgumentException("La categoria no existe");
        }
    }

    public PageResponse<CategoryResponse> getAllCategories(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAllC(pageRequest);
        return new PageResponse<>(categoryPage.map(CategoryResponse::new));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllC();
    }

    public Category updateCategory(Long id, CreateCategoryRequest categoryRequest) {
        Category category = categoryRepository.findByIdC(id);
        if (category != null) {
            category.setCategoryName(categoryRequest.getName());
            return categoryRepository.save(category);
        } else {
            throw new IllegalArgumentException("Categoria no encontrada");
        }
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findByIdC(categoryId);
        if (category != null){
            categoryRepository.softDelete(categoryId);
        } else {
            throw new IllegalArgumentException("Categoria no encontrada");
        }
    }

}
