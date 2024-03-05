package com.api.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

        @GetMapping("/admin")
        public ResponseEntity<Object> getCategoryListAdmin(
                        @RequestParam(name = "limit", defaultValue = "-1") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword,
                        @RequestParam(name = "parent", defaultValue = "") String categoryParentSlug) {

                CategoryParams params = CategoryParams.builder().categoryParentSlug(categoryParentSlug).keyword(keyword)
                                .sortBy(sortBy).sortType(sortType).page(Integer.parseInt(page))
                                .pageSize(Integer.parseInt(limit)).build();

                Page<Category> categoryPage = categoryService.findCategoryList(params, true);

                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<Category>builder()
                                                                .rows(categoryPage.getContent())
                                                                .totalPages(categoryPage.getTotalPages())
                                                                .count(categoryPage.getTotalElements())
                                                                .build())
                                                .build());
        }

        @GetMapping()
        public ResponseEntity<Object> getCategoryList(
                        @RequestParam(name = "limit", defaultValue = "-1") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword,
                        @RequestParam(name = "parent", defaultValue = "") String categoryParentSlug) {

                CategoryParams params = CategoryParams.builder().categoryParentSlug(categoryParentSlug).keyword(keyword)
                                .sortBy(sortBy).sortType(sortType).page(Integer.parseInt(page))
                                .pageSize(Integer.parseInt(limit)).build();

                Page<Category> categoryPage = categoryService.findCategoryList(params, false);

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

        @GetMapping("/slug/{slug}")
        public ResponseEntity<Object> findCategoryBySlug(@PathVariable("slug") String slug) {
                Optional<Category> categoryOptional = this.categoryService.findBySlug(slug);
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
                        categoryOptional = categoryService.update(id, body);
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

        @DeleteMapping()
        public ResponseEntity<Object> deleteCategories(@RequestParam("ids") String idsStr) {

                List<Long> ids = new ArrayList<>();

                String[] idsStrSplitted = idsStr.split("_");

                for (int i = 0; i < idsStrSplitted.length; i++) {
                        ids.add(Long.valueOf(idsStrSplitted[i]));
                }

                List<Category> categoryList = this.categoryService.findByIdIn(ids);

                if (categoryList.size() < ids.size()) {
                        return ResponseEntity.status(ApiConstant.STATUS_404)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Not Found")
                                                        .build());
                }

                boolean isDeleted = this.categoryService.deleteMultiple(ids);
                if (isDeleted) {
                        // for (int i = 0; i < categoryList.size(); i++) {
                        // cloudinaryService.deleteImage(categoryList.get(i).getImageUrl());
                        // }
                        return ResponseEntity.status(ApiConstant.STATUS_200)
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
