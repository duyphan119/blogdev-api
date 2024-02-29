package com.api.article_reply_comment;

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

import com.api.article_comment.ArticleComment;
import com.api.article_comment.ArticleCommentService;
import com.api.auth.AuthenticationService;
import com.api.user.CustomUserDetails;
import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;
import com.api.utils.PaginatedData;

@RestController
@RequestMapping("/article-reply-comment")
public class ArticleReplyCommentController {

        @Autowired
        private ArticleReplyCommentService articleReplyCommentService;

        @Autowired
        private ArticleCommentService articleCommentService;

        @Autowired
        private AuthenticationService authenticationService;

        @GetMapping()
        public ResponseEntity<Object> getArticleReplyCommentList(
                        @RequestParam(name = "limit", defaultValue = "10") String limit,
                        @RequestParam(name = "p", defaultValue = "1") String page,
                        @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
                        @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
                        @RequestParam(name = "q", defaultValue = "") String keyword,
                        @RequestParam(name = "article_comment_id", defaultValue = "0") String articleCommentIdStr) {
                Integer pageSize = Integer.parseInt(limit);
                Page<ArticleReplyComment> articleReplyCommentPage = Page.empty();
                if (pageSize.compareTo(0) > 0) {
                        articleReplyCommentPage = this.articleReplyCommentService
                                        .paginateByArticleCommentId(
                                                        Long.valueOf(articleCommentIdStr),
                                                        Integer.parseInt(limit),
                                                        Integer.parseInt(page),
                                                        sortBy,
                                                        sortType, keyword);
                }

                return ResponseEntity.status(ApiConstant.STATUS_200)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                                .data(PaginatedData.<ArticleReplyComment>builder()
                                                                .rows(articleReplyCommentPage.getContent())
                                                                .totalPages(articleReplyCommentPage.getTotalPages())
                                                                .count(articleReplyCommentPage.getTotalElements())
                                                                .build())
                                                .build());
        }

        @PostMapping()
        public ResponseEntity<Object> createArticleReplyComment(@RequestBody ArticleReplyComment body) {
                Optional<CustomUserDetails> userDetailsOptional = this.authenticationService.getUserDetails();
                if (userDetailsOptional.isPresent()) {
                        body.setUser(userDetailsOptional.get().getUser());
                        Optional<ArticleComment> articleCommentOptional = this.articleCommentService
                                        .findById(body.getArticleComment().getId());
                        if (articleCommentOptional.isPresent()) {
                                articleCommentOptional.get()
                                                .setReplyCount(articleCommentOptional.get().getReplyCount() + 1);
                                Optional<ArticleComment> newArticleCommentOptional = this.articleCommentService
                                                .update(articleCommentOptional.get().getId(),
                                                                articleCommentOptional.get());
                                body.setArticleComment(articleCommentOptional.get());
                                if (newArticleCommentOptional.isPresent()) {

                                        Optional<ArticleReplyComment> articleReplyCommentOptional = this.articleReplyCommentService
                                                        .create(body);
                                        if (articleReplyCommentOptional.isPresent()) {
                                                return ResponseEntity.status(ApiConstant.STATUS_201)
                                                                .body(ApiResponse.builder()
                                                                                .message(ApiConstant.MSG_SUCCESS)
                                                                                .data(articleReplyCommentOptional.get())
                                                                                .build());
                                        }
                                }
                        } else {

                                return ResponseEntity.status(ApiConstant.STATUS_400)
                                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                                .data("Bad Request")
                                                                .build());
                        }
                } else {

                        return ResponseEntity.status(ApiConstant.STATUS_401)
                                        .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                        .data("Unauthorized")
                                                        .build());
                }

                return ResponseEntity.status(ApiConstant.STATUS_500)
                                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                                                .data("Something went wrong")
                                                .build());
        }

        @PatchMapping("/{id}")
        public ResponseEntity<Object> updateArticleReplyComment(@RequestBody ArticleReplyComment body,
                        @PathVariable("id") Long id) {
                Optional<ArticleReplyComment> articleReplyCommentOptional = this.articleReplyCommentService
                                .findById(id);

                if (articleReplyCommentOptional.isPresent()) {
                        Optional<CustomUserDetails> userDetailsOptional = this.authenticationService.getUserDetails();
                        if (userDetailsOptional.isPresent()
                                        && userDetailsOptional.get().getUser().getId()
                                                        .equals(articleReplyCommentOptional.get()
                                                                        .getUser()
                                                                        .getId())) {
                                body.setUser(userDetailsOptional.get().getUser());

                                articleReplyCommentOptional = this.articleReplyCommentService.update(body);
                                if (articleReplyCommentOptional.isPresent()) {
                                        return ResponseEntity.status(ApiConstant.STATUS_201)
                                                        .body(ApiResponse.builder()
                                                                        .message(ApiConstant.MSG_SUCCESS)
                                                                        .data(articleReplyCommentOptional.get())
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
        public ResponseEntity<Object> deleteArticleReplyComment(@PathVariable("id") Long id) {
                Optional<ArticleReplyComment> articleReplyCommentOptional = this.articleReplyCommentService
                                .findById(id);

                if (articleReplyCommentOptional.isPresent()) {
                        Optional<CustomUserDetails> userDetailsOptional = this.authenticationService.getUserDetails();

                        if (userDetailsOptional.isPresent()) {
                                if (userDetailsOptional.get().getUser().getId().equals(articleReplyCommentOptional.get()
                                                .getUser()
                                                .getId())) {

                                        Boolean isDeleted = this.articleReplyCommentService.delete(id);
                                        if (isDeleted) {
                                                ArticleComment articleComment = articleReplyCommentOptional.get()
                                                                .getArticleComment();
                                                articleComment.setReplyCount(articleComment.getReplyCount() - 1);
                                                this.articleCommentService.update(articleComment.getId(),
                                                                articleComment);
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
