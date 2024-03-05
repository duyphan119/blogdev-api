package com.api.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);

    List<Category> findByIdIn(List<Long> ids);

    Page<Category> findByIsPublicAndNameIgnoreCaseContainingAndParent_Slug(Boolean isPublic, String name, String slug,
            Pageable pageable);

    Page<Category> findByIsPublicAndNameIgnoreCaseContaining(Boolean isPublic, String name,
            Pageable pageable);

    Page<Category> findByNameIgnoreCaseContainingAndParent_Slug(String name, String slug,
            Pageable pageable);

    Page<Category> findByNameIgnoreCaseContaining(String name,
            Pageable pageable);
}
