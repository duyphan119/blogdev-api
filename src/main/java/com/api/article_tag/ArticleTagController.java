package com.api.article_tag;

import java.util.Optional;
import java.util.Set;

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
import com.api.role.Role;
import com.api.user.CustomUserDetails;
import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;
import com.api.utils.PaginatedData;

@RestController
@RequestMapping("article-tag")
public class ArticleTagController {
    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping()
    public ResponseEntity<Object> paginate(@RequestParam(name = "limit", defaultValue = "10") String limit,
            @RequestParam(name = "p", defaultValue = "1") String page,
            @RequestParam(name = "sort_by", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sort_type", defaultValue = "desc") String sortType,
            @RequestParam(name = "q", defaultValue = "") String keyword) {
        Page<ArticleTag> articleTagPage = this.articleTagService.paginate(Integer.parseInt(limit),
                Integer.parseInt(page), sortBy, sortType, keyword);

        return ResponseEntity.ok(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                .data(PaginatedData.<ArticleTag>builder().rows(articleTagPage.getContent())
                        .count(articleTagPage.getTotalElements()).totalPages(articleTagPage.getTotalPages()).build())
                .build());
    }

    @PostMapping()
    public ResponseEntity<Object> createArticleTag(@RequestBody ArticleTag body) {
        if (body.getSlug() == null) {
            body.setSlug(this.articleTagService.generateSlug(body.getName()));
        }
        Optional<ArticleTag> articleTagOptional = this.articleTagService.create(body);
        if (articleTagOptional.isPresent()) {
            return ResponseEntity.status(ApiConstant.STATUS_201)
                    .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                            .data(articleTagOptional.get())
                            .build());
        }
        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateArticleTag(@PathVariable("id") Long id, @RequestBody ArticleTag body) {
        CustomUserDetails userDetails = this.authenticationService.getUserDetails().get();

        Optional<ArticleTag> articleTagOptional = this.articleTagService.findById(id);

        if (articleTagOptional.isPresent()) {
            if (body.getSlug().equals(articleTagOptional.get().getSlug())) {
                if (!body.getName().equals(articleTagOptional.get().getName()))
                    body.setSlug(this.articleTagService.generateSlug(body.getName()));
            }
            boolean isRoleAdmin = false;

            Set<Role> roles = userDetails.getUser().getRoles();

            for (Role accountRole : roles) {
                if (accountRole.getName().equals("ADMIN")) {
                    isRoleAdmin = true;
                }
            }

            if (userDetails.getUser().getId().equals(articleTagOptional.get().getCreatedBy()) || isRoleAdmin) {
                articleTagOptional = this.articleTagService.update(id, body);

                return ResponseEntity
                        .ok(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(articleTagOptional.get())
                                .build());
            }
        }

        return ResponseEntity.status(ApiConstant.STATUS_404)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Not found")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {

        Optional<ArticleTag> articleTagOptional = this.articleTagService.findById(id);

        if (articleTagOptional.isPresent()) {
            return ResponseEntity
                    .ok(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(articleTagOptional.get()).build());
        }

        return ResponseEntity.status(ApiConstant.STATUS_404)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Not found")
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteArticleTag(@PathVariable("id") Long id) {
        CustomUserDetails userDetails = this.authenticationService.getUserDetails().get();

        Long userId = userDetails.getUser().getId();

        Optional<ArticleTag> articleTagOptional = this.articleTagService.findById(id);

        if (articleTagOptional.isPresent()) {
            ArticleTag articleTag = articleTagOptional.get();

            boolean isRoleAdmin = false;

            Set<Role> roles = userDetails.getUser().getRoles();

            for (Role accountRole : roles) {
                if (accountRole.getName().equals("ADMIN")) {
                    isRoleAdmin = true;
                }
            }

            if (userId.equals(articleTag.getCreatedBy()) || isRoleAdmin) {
                boolean isDeleted = this.articleTagService.delete(articleTag.getId());

                if (isDeleted) {

                    return ResponseEntity.status(ApiConstant.STATUS_200)
                            .body(ApiResponse.builder().message(ApiConstant.MSG_SUCCESS)
                                    .data("Deleted")
                                    .build());
                }

            }

        }

        return ResponseEntity.status(ApiConstant.STATUS_500)
                .body(ApiResponse.builder().message(ApiConstant.MSG_ERROR)
                        .data("Something went wrong")
                        .build());
    }
}
