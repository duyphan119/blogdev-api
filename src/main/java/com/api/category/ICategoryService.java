package com.api.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.api.utils.ICrudService;

public interface ICategoryService extends ICrudService<Category, Long> {

    String generateSlug(String name);

    List<Category> findAll(Sort sort);

    Optional<Category> findBySlug(String slug);
}
