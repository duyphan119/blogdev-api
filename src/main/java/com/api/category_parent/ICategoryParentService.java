package com.api.category_parent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ICategoryParentService {
    Optional<CategoryParent> create(CategoryParent category);

    Page<CategoryParent> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);

    Optional<CategoryParent> findById(Long id);

    Optional<CategoryParent> update(CategoryParent category);

    boolean delete(Long id);

    String generateSlug(String name);

    List<CategoryParent> findAll(Sort sort);

    Page<CategoryParent> findCategoryParentList(CategoryParentParams params, boolean isAdmin);

    boolean deleteMultiple(List<Long> ids);
}
