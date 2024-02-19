package com.api.category;

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
@RequestMapping("/category")
public class CategoryController {
        @Autowired
        private CategoryService categoryService;

        @GetMapping()
        public ResponseEntity<Object> getCategoryList(
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
                                                        .data(this.categoryService
                                                                        .findAll(sort))
                                                        .build());
                }
                Page<Category> categoryPage = categoryService.paginate(Integer.parseInt(limit), Integer.parseInt(page),
                                sortBy,
                                sortType, keyword);
                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<Category>builder()
                                                                .rows(categoryPage.getContent())
                                                                .totalPages(categoryPage.getTotalPages())
                                                                .count(categoryPage.getTotalElements())
                                                                .build())
                                                .build());
        }

        @PostMapping()
        public ResponseEntity<Object> createCategory(@RequestBody Category body) {
                if (body.getSlug() == null) {
                        body.setSlug(categoryService.generateSlug(body.getName()));
                }
                Optional<Category> categoryOptional = categoryService.create(body);
                if (categoryOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_201)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(categoryOptional.get())
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Object> findCategoryById(@PathVariable("id") Long id) {
                Optional<Category> categoryOptional = this.categoryService.findById(id);
                if (categoryOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_200)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(categoryOptional.get())
                                                        .build());
                }
                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @PatchMapping("/{id}")
        public ResponseEntity<Object> updateCategory(@PathVariable("id") Long id, @RequestBody Category body) {
                Optional<Category> categoryOptional = this.categoryService.findById(id);
                if (categoryOptional.isPresent()) {
                        body.setId(id);
                        body.setCreatedAt(categoryOptional.get().getCreatedAt());
                        if (!body.getName().equals(categoryOptional.get().getName())) {
                                if (body.getSlug() == null || body.getSlug().equals(categoryOptional.get().getSlug())) {
                                        body.setSlug(categoryService.generateSlug(body.getName()));
                                }
                        }
                        categoryOptional = categoryService.create(body);
                        if (categoryOptional.isPresent()) {
                                return ResponseEntity.status(ApiConstant.STATUS_201)
                                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                                .data(categoryOptional.get())
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
        public ResponseEntity<Object> deleteCategory(@PathVariable("id") Long id) {
                Boolean isDeleted = this.categoryService.delete(id);
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
