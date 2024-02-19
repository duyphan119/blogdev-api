package com.api.article;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface IArticleService {
    Page<Article> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword);

    Optional<Article> create(Article article);

    String generateSlug(String name);

    ArticleResponse convertToArticleResponse(Article article);

    ArticleDetailResponse convertToArticleDetailResponse(Article article);

    Optional<Article> findBySlug(String slug);

    Optional<Article> update(Article article);

    Optional<Article> findById(Long id);

    Page<Article> paginateRecommendArticleList(String articleSlug);

    Page<Article> paginateAuthorArticleList(Long userId, Integer limit, Integer page, String sortBy, String sortType,
            String keyword);

    boolean delete(Long id);
}
