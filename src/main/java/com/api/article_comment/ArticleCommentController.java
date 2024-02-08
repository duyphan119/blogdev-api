package com.api.article_comment;

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
import com.api.user.CustomUserDetails;
import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;
import com.api.utils.PaginatedData;

@RestController
@RequestMapping("/article-comment")
public class ArticleCommentController {
        @Autowired
        private ArticleCommentService articleCommentService;

        @Autowired
        private AuthenticationService authenticationService;

        @GetMapping()
        public ResponseEntity<Object> getArticleCommentList(
                        @RequestParam(name = "limit", defaultValue = "10") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword,
                        @RequestParam(name = "article_slug", defaultValue = "") String articleSlug) {
                Page<ArticleComment> articleCommentPage = articleSlug.equals(
                                "") ? this.articleCommentService.paginate(Integer.parseInt(limit), Integer.parseInt(page),
                                                sortBy,
                                                sortType, keyword)
                                                : this.articleCommentService.paginateByArticleSlug(articleSlug,
                                                                Integer.parseInt(limit),
                                                                Integer.parseInt(page),
                                                                sortBy,
                                                                sortType, keyword);
                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<ArticleComment>builder()
                                                                .rows(articleCommentPage.getContent())
                                                                .totalPages(articleCommentPage.getTotalPages())
                                                                .count(articleCommentPage.getTotalElements())
                                                                .build())
                                                .build());
        }

        @PostMapping()
        public ResponseEntity<Object> createArticleComment(@RequestBody ArticleComment body) {
                Optional<CustomUserDetails> userDetailsOptional = this.authenticationService.getUserDetails();
                if (userDetailsOptional.isPresent()) {
                        body.setUser(userDetailsOptional.get().getUser());
                        Optional<ArticleComment> articleCommentOptional = this.articleCommentService.create(body);
                        if (articleCommentOptional.isPresent()) {
                                return ResponseEntity.status(ApiConstant.STATUS_201)
                                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                                .data(articleCommentOptional.get())
                                                                .build());
                        }
                }
                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @PatchMapping("/{id}")
        public ResponseEntity<Object> updateArticleComment(@RequestBody ArticleComment body,
                        @PathVariable("id") Long id) {
                Optional<ArticleComment> articleCommentOptional = this.articleCommentService
                                .findById(id);

                if (articleCommentOptional.isPresent()) {
                        Optional<CustomUserDetails> userDetailsOptional = this.authenticationService.getUserDetails();
                        if (userDetailsOptional.isPresent()
                                        && userDetailsOptional.get().getUser().getId()
                                                        .equals(articleCommentOptional.get()
                                                                        .getUser()
                                                                        .getId())) {
                                body.setUser(userDetailsOptional.get().getUser());
                                System.out.println(body.getId());
                                System.out.println(body.getUser().getId());

                                articleCommentOptional = this.articleCommentService.update(body);
                                if (articleCommentOptional.isPresent()) {
                                        return ResponseEntity.status(ApiConstant.STATUS_201)
                                                        .body(ApiResponse.builder()
                                                                        .message(ApiConstant.MSG_SUCCESS)
                                                                        .data(articleCommentOptional.get())
                                                                        .build());
                                }
                        } else {
                                System.out.println("Không");
                                return ResponseEntity.status(ApiConstant.STATUS_403)
                                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                                .data("Forbidden")
                                                                .build());
                        }

                } else {
                        return ResponseEntity.status(ApiConstant.STATUS_404)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Not Found")
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500).body(ApiResponse.builder()
                                .message(ApiConstant.MSG_ERROR).data("Something went wrong").build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Object> deleteArticleComment(@PathVariable("id") Long id) {
                Optional<ArticleComment> articleCommentOptional = this.articleCommentService.findById(id);

                if (articleCommentOptional.isPresent()) {
                        Optional<CustomUserDetails> userDetailsOptional = this.authenticationService.getUserDetails();

                        if (userDetailsOptional.isPresent()) {
                                if (userDetailsOptional.get().getUser().getId().equals(articleCommentOptional.get()
                                                .getUser()
                                                .getId())) {

                                        Boolean isDeleted = this.articleCommentService.delete(id);
                                        if (isDeleted) {
                                                return ResponseEntity.status(ApiConstant.STATUS_200)
                                                                .body(ApiResponse.builder()
                                                                                .message(ApiConstant.MSG_SUCCESS)
                                                                                .data("Deleted")
                                                                                .build());
                                        }
                                } else {
                                        return ResponseEntity.status(ApiConstant.STATUS_403)
                                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                                        .data("Forbidden")
                                                                        .build());
                                }
                        } else {
                                return ResponseEntity.status(ApiConstant.STATUS_401)
                                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                                .data("Unauthorized")
                                                                .build());
                        }

                } else {
                        return ResponseEntity.status(ApiConstant.STATUS_404)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Not Found")
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }
}
