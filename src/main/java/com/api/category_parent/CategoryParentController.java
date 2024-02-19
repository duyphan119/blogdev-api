package com.api.category_parent;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;
import com.api.utils.PaginatedData;

@RestController
@RequestMapping("category-parent")
public class CategoryParentController {
    @Autowired
    private CategoryParentService categoryParentService;

    @GetMapping()
    public ResponseEntity<Object> getCategoryParentList(
            @RequestParam(name = "limit", defaultValue = "10") String limit,
            @RequestParam(name = "p", defaultValue = "1") String page,
            @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
            @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
            @RequestParam(name = "q", defaultValue = "") String keyword,
            @RequestParam(name = "no_paginate", defaultValue = "false") String noPaginateStr) {
        if (noPaginateStr.equals("true")) {
            Sort sort = Sort.by(sortBy);
            if (sortType.equals("desc")) {
                sort = sort.descending();
            } else {
                sort = sort.ascending();
            }
            return ResponseEntity.status(ApiConstant.STATUS_200)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data(this.categoryParentService
                                    .findAll(sort))
                            .build());
        }
        Page<CategoryParent> categoryParentPage = categoryParentService.paginate(Integer.parseInt(limit),
                Integer.parseInt(page),
                sortBy,
                sortType, keyword);
        return ResponseEntity.status(ApiConstant.STATUS_200)
                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                        .data(PaginatedData.<CategoryParent>builder()
                                .rows(categoryParentPage.getContent())
                                .totalPages(categoryParentPage.getTotalPages())
                                .count(categoryParentPage.getTotalElements())
                                .build())
                        .build());
    }

    @PostMapping()
    public ResponseEntity<Object> createCategoryParent(@RequestBody CategoryParent body) {
        if (body.getSlug() == null) {
            body.setSlug(categoryParentService.generateSlug(body.getName()));
        }
        Optional<CategoryParent> categoryParentOptional = categoryParentService.create(body);
        if (categoryParentOptional.isPresent()) {
            return ResponseEntity.status(ApiConstant.STATUS_201)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data(categoryParentOptional.get())
                            .build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findCategoryParentById(@PathVariable("id") Long id) {
        Optional<CategoryParent> categoryParentOptional = this.categoryParentService.findById(id);
        if (categoryParentOptional.isPresent()) {
            return ResponseEntity.status(ApiConstant.STATUS_200)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data(categoryParentOptional.get())
                            .build());
        }
        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCategoryParent(@PathVariable("id") Long id, @RequestBody CategoryParent body) {
        Optional<CategoryParent> categoryParentOptional = this.categoryParentService.findById(id);
        if (categoryParentOptional.isPresent()) {
            body.setId(id);
            body.setCreatedAt(categoryParentOptional.get().getCreatedAt());
            if (!body.getName().equals(categoryParentOptional.get().getName())) {
                if (body.getSlug() == null || body.getSlug().equals(categoryParentOptional.get().getSlug())) {
                    body.setSlug(categoryParentService.generateSlug(body.getName()));
                }
            }
            categoryParentOptional = categoryParentService.create(body);
            if (categoryParentOptional.isPresent()) {
                return ResponseEntity.status(ApiConstant.STATUS_201)
                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                .data(categoryParentOptional.get())
                                .build());
            }
        } else {
            return ResponseEntity.status(ApiConstant.STATUS_404)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                            .data("Not found")
                            .build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategoryParent(@PathVariable("id") Long id) {
        Boolean isDeleted = this.categoryParentService.delete(id);
        if (isDeleted) {
            return ResponseEntity.status(ApiConstant.STATUS_201)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data("Deleted")
                            .build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }
}
