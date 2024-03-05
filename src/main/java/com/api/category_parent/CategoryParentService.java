package com.api.category_parent;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.utils.Helper;

@Service
public class CategoryParentService implements ICategoryParentService {

    @Autowired
    private CategoryParentRepository categoryParentRepo;

    @Autowired
    private Helper helper;

    @Override
    public Optional<CategoryParent> create(CategoryParent categoryParent) {
        try {

            return Optional.of(this.categoryParentRepo.save(categoryParent));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<CategoryParent> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return this.categoryParentRepo.findAll(pageable);
    }

    @Override
    public Optional<CategoryParent> findById(Long id) {
        return this.categoryParentRepo.findById(id);
    }

    @Override
    public Optional<CategoryParent> update(CategoryParent categoryParent) {
        try {
            return Optional.of(this.categoryParentRepo.save(categoryParent));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.categoryParentRepo.deleteById(id);
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
    public List<CategoryParent> findAll(Sort sort) {
        return this.categoryParentRepo.findAll(sort);
    }

    @Override
    public Page<CategoryParent> findCategoryParentList(CategoryParentParams params, boolean isAdmin) {
        Integer pageSize = params.getPageSize();
        Integer page = params.getPage();
        String sortBy = params.getSortBy();
        String sortType = params.getSortType();
        String keyword = params.getKeyword();
        Pageable pageable = Pageable.unpaged();

        if (!pageSize.equals(-1)) {
            pageable = helper.generatePageable(pageSize, page, sortBy, sortType);
        }

        if (isAdmin) {
            return this.categoryParentRepo.findByNameIgnoreCaseContaining(keyword, pageable);
        }

        return this.categoryParentRepo.findByIsPublicAndNameIgnoreCaseContaining(true, keyword, pageable);
    }

    @Override
    public boolean deleteMultiple(List<Long> ids) {
        try {
            this.categoryParentRepo.deleteAllByIdInBatch(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
