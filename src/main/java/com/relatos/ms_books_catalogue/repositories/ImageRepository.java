package com.relatos.ms_books_catalogue.repositories;

import com.relatos.ms_books_catalogue.domains.Image;
import com.relatos.ms_books_catalogue.repositories.commons.SoftRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends SoftRepository<Image> {
}
