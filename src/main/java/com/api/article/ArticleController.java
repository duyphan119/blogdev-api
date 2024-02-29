package com.api.article;

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

import com.api.auth.AuthenticationService;
import com.api.upload.CloudinaryService;
import com.api.user.Author;
import com.api.user.CustomUserDetails;
import com.api.user.UserService;
import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;
import com.api.utils.PaginatedData;

@RestController
@RequestMapping("article")
public class ArticleController {

        @Autowired
        private ArticleService articleService;

        @Autowired
        private AuthenticationService authenticationService;

        @Autowired
        private CloudinaryService cloudinaryService;

        @Autowired
        private UserService userService;

        @GetMapping()
        public ResponseEntity<Object> getArticleList(
                        @RequestParam(name = "limit", defaultValue = "10") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword,
                        @RequestParam(name = "cat", defaultValue = "") String categorySlug) {
                Page<Article> articlePage = Page.empty();
                if (!categorySlug.equals("")) {
                        articlePage = articleService.paginateByCategorySlug(Integer.parseInt(limit),
                                        Integer.parseInt(page),
                                        sortBy,
                                        sortType, categorySlug);
                } else {

                        articlePage = articleService.paginate(Integer.parseInt(limit), Integer.parseInt(page),
                                        sortBy,
                                        sortType, keyword);
                }
                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<Article>builder()
                                                                .rows(articlePage.getContent())
                                                                .totalPages(articlePage.getTotalPages())
                                                                .count(articlePage.getTotalElements())
                                                                .build())
                                                .build());
        }

        @GetMapping("/author/{id}")
        public ResponseEntity<Object> getArticleListByAuthor(@PathVariable("id") Long id,
                        @RequestParam(name = "limit", defaultValue = "10") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword) {
                Optional<Author> authorOptional = this.userService.getAuthor(id);
                Page<Article> articlePage = articleService.paginateAuthorArticleList(
                                id, Integer.parseInt(limit),
                                Integer.parseInt(page),
                                sortBy,
                                sortType, keyword);
                AuthorArticlesResponse authorArticlesResponse = AuthorArticlesResponse.builder()
                                .rows(articlePage.getContent())
                                .totalPages(articlePage.getTotalPages())
                                .count(articlePage.getTotalElements())
                                .build();
                if (authorOptional.isPresent()) {
                        authorArticlesResponse.setAuthor(authorOptional.get());
                }

                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(authorArticlesResponse)
                                                .build());

        }

        @GetMapping("/author")
        public ResponseEntity<Object> getAuthorArticleList(
                        @RequestParam(name = "limit", defaultValue = "10") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword) {

                Optional<CustomUserDetails> userDetailsOptional = authenticationService.getUserDetails();
                if (userDetailsOptional.isPresent()) {
                        Page<Article> articlePage = articleService.paginateAuthorArticleList(
                                        userDetailsOptional.get().getUser().getId(), Integer.parseInt(limit),
                                        Integer.parseInt(page),
                                        sortBy,
                                        sortType, keyword);
                        return ResponseEntity.status(ApiConstant.STATUS_200)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(PaginatedData.<Article>builder()
                                                                        .rows(articlePage.getContent())
                                                                        .totalPages(articlePage.getTotalPages())
                                                                        .count(articlePage.getTotalElements())
                                                                        .build())
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @GetMapping("/recommend/slug/{slug}")
        public ResponseEntity<Object> getRecommendArticleList(@PathVariable("slug") String slug) {
                Page<Article> articlePage = this.articleService.paginateRecommendArticleList(slug);
                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<Article>builder()
                                                                .rows(articlePage.getContent())
                                                                .totalPages(articlePage.getTotalPages())
                                                                .count(articlePage.getTotalElements())
                                                                .build())
                                                .build());
        }

        @GetMapping("/id/{id}")
        public ResponseEntity<Object> getArticleById(@PathVariable("id") Long id) {
                Optional<Article> articleOptional = articleService.findById(id);

                if (articleOptional.isPresent()) {
                        ArticleDetailResponse articleDetailResponse = articleService.convertToArticleDetailResponse(
                                        articleOptional.get());
                        return ResponseEntity.status(ApiConstant.STATUS_200)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(articleDetailResponse)
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_404)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Not found")
                                                .build());
        }

        @GetMapping("/slug/{slug}")
        public ResponseEntity<Object> getArticleBySlug(@PathVariable("slug") String slug) {
                Optional<Article> articleOptional = articleService.findBySlug(slug);

                if (articleOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_200)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(articleService.convertToArticleDetailResponse(
                                                                        articleOptional.get()))
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_404)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Not found")
                                                .build());
        }

        @PostMapping()
        public ResponseEntity<Object> createArticle(@RequestBody Article body) {
                Optional<CustomUserDetails> userDetails = authenticationService.getUserDetails();
                if (body.getSlug() == null) {
                        body.setSlug(articleService.generateSlug(body.getTitle()));
                }
                body.setIsPublic(true);
                body.setAuthor(userDetails.get().getUser());
                Optional<Article> articleOptional = articleService.create(body);
                if (articleOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_201)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(articleOptional.get())
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @PostMapping("/author")
        public ResponseEntity<Object> createAuthorArticle(@RequestBody Article body) {
                Optional<CustomUserDetails> userDetails = authenticationService.getUserDetails();
                if (body.getSlug() == null) {
                        body.setSlug(articleService.generateSlug(body.getTitle()));
                }
                body.setAuthor(userDetails.get().getUser());
                Optional<Article> articleOptional = articleService.create(body);
                if (articleOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_201)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(articleOptional.get())
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @PatchMapping("/author/{id}")
        public ResponseEntity<Object> updateAuthorArticle(@PathVariable("id") Long id, @RequestBody Article body) {
                System.out.println("id" + id);
                Optional<Article> articleOptional = this.articleService.findById(id);
                CustomUserDetails userDetails = authenticationService.getUserDetails().get();

                if (articleOptional.isEmpty()) {
                        return ResponseEntity.status(ApiConstant.STATUS_404)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Not Found")
                                                        .build());
                }
                body.setId(id);

                if (body.getSlug().equals(articleOptional.get().getSlug())) {
                        if (!body.getTitle().equals(articleOptional.get().getTitle()))
                                body.setSlug(articleService.generateSlug(body.getTitle()));
                }

                if (!body.getAuthor().getId().equals(userDetails.getUser().getId())) {
                        return ResponseEntity.status(ApiConstant.STATUS_401)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Unauthorized")
                                                        .build());
                }
                body.setAuthor(userDetails.getUser());
                articleOptional = articleService.update(id, body);
                if (articleOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_200)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(articleOptional.get())
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @DeleteMapping("/author/{id}")
        public ResponseEntity<Object> deleteAuthorArticle(@PathVariable("id") Long id) {
                Optional<Article> articleOptional = this.articleService.findById(id);
                CustomUserDetails userDetails = authenticationService.getUserDetails().get();

                if (articleOptional.isEmpty()) {
                        return ResponseEntity.status(ApiConstant.STATUS_404)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Not Found")
                                                        .build());
                }

                if (!articleOptional.get().getAuthor().getId().equals(userDetails.getUser().getId())) {
                        return ResponseEntity.status(ApiConstant.STATUS_401)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Unauthorized")
                                                        .build());
                }

                boolean isDeleted = this.articleService.delete(id);
                if (isDeleted) {
                        cloudinaryService.deleteImage(articleOptional.get().getImageUrl());
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
