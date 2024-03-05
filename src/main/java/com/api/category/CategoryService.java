package com.api.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.utils.Helper;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private Helper helper;

    @Override
    public Optional<Category> create(Category category) {
        try {
            return Optional.of(this.categoryRepo.save(category));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Category> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return this.categoryRepo.findAll(pageable);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return this.categoryRepo.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.categoryRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generateSlug(String name) {
        return helper.generateSlug(name);
    }

    @Override
    public List<Category> findAll(Sort sort) {
        return this.categoryRepo.findAll(sort);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        try {
            return this.categoryRepo.findBySlug(slug);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Category> update(Long id, Category body) {
        try {
            Optional<Category> categoryOptional = this.categoryRepo.findById(id);
            if (categoryOptional.isPresent()) {
                body.setId(id);
                return Optional.of(this.categoryRepo.save(body));
            }
        } catch (Exception e) {

        }
        return Optional.empty();
    }

    @Override
    public boolean deleteMultiple(List<Long> ids) {
        try {
            this.categoryRepo.deleteAllByIdInBatch(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Category> findByIdIn(List<Long> ids) {
        return this.categoryRepo.findByIdIn(ids);
    }

    @Override
    public Page<Category> findCategoryList(CategoryParams params, boolean isAdmin) {
        Integer pageSize = params.getPageSize();
        Integer page = params.getPage();
        String sortBy = params.getSortBy();
        String sortType = params.getSortType();
        String keyword = params.getKeyword();
        String categoryParentSlug = params.getCategoryParentSlug();
        Pageable pageable = Pageable.unpaged();

        if (!pageSize.equals(-1)) {
            pageable = helper.generatePageable(pageSize, page, sortBy, sortType);
        }

        if (!categoryParentSlug.isEmpty()) {
            if (isAdmin) {
                return this.categoryRepo.findByNameIgnoreCaseContainingAndParent_Slug(keyword, categoryParentSlug,
                        pageable);
            }

            return this.categoryRepo.findByIsPublicAndNameIgnoreCaseContainingAndParent_Slug(true, keyword,
                    categoryParentSlug, pageable);
        }

        if (isAdmin) {
            return this.categoryRepo.findByNameIgnoreCaseContaining(keyword, pageable);
        }

        return this.categoryRepo.findByIsPublicAndNameIgnoreCaseContaining(true, keyword, pageable);
    }

}
