package com.api.article;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface IArticleService {
    Page<Article> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);

    Optional<Article> create(Article article);

    String generateSlug(String name);

    ArticleResponse convertToArticleResponse(Article article);

    Optional<Article> findBySlug(String slug);
}
