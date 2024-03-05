package com.api.category_parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryParentRepository extends JpaRepository<CategoryParent, Long> {
    Page<CategoryParent> findByIsPublicAndNameIgnoreCaseContaining(Boolean isPublic, String name,
            Pageable pageable);

    Page<CategoryParent> findByNameIgnoreCaseContaining(String name,
            Pageable pageable);
}
