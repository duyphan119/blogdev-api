package com.api.article;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth.AuthenticationService;
import com.api.user.CustomUserDetails;
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

        @GetMapping()
        public ResponseEntity<Object> getArticleList(
                        @RequestParam(name = "limit", defaultValue = "10") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword) {
                Page<Article> articlePage = articleService.paginate(Integer.parseInt(limit), Integer.parseInt(page),
                                sortBy,
                                sortType, keyword);
                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<ArticleResponse>builder()
                                                                .rows(articlePage.getContent().stream().map(article -> {
                                                                        article.setContent(null);
                                                                        return articleService.convertToArticleResponse(
                                                                                        article);
                                                                }).collect(Collectors.toList()))
                                                                .totalPages(articlePage.getTotalPages())
                                                                .count(articlePage.getTotalElements())
                                                                .build())
                                                .build());
        }

        @GetMapping("/slug/{slug}")
        public ResponseEntity<Object> getArticleBySlug(@PathVariable("slug") String slug) {
                Optional<Article> articleOptional = articleService.findBySlug(slug);

                if (articleOptional.isPresent()) {
                        return ResponseEntity.status(ApiConstant.STATUS_200)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                        .data(articleService.convertToArticleResponse(
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
}
