package com.api.category;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface ICategoryService {
    Optional<Category> create(Category category);

    Page<Category> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);

    Optional<Category> findById(Long id);

    Optional<Category> update(Category category);

    boolean delete(Long id);

    String generateSlug(String name);
}
