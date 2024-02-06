package com.api.category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Optional<Category> update(Category category) {
        try {
            return Optional.of(this.categoryRepo.save(category));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean delete(Long id) {
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

}
