package com.api.article_comment;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface IArticleCommentService {
    Optional<ArticleComment> create(ArticleComment articleComment);

    boolean delete(Long id);

    Page<ArticleComment> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);

    Page<ArticleComment> paginateByArticleSlug(String articleSlug, Integer limit, Integer page, String sortBy,
            String sortType, String keyword);

    Optional<ArticleComment> findById(Long id);

    Optional<ArticleComment> update(ArticleComment articleComment);
}
