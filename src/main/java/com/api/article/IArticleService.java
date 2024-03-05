package com.api.article;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.api.utils.ICrudService;

public interface IArticleService extends ICrudService<Article, Long> {

        String generateSlug(String name);

        ArticleDetailResponse convertToArticleDetailResponse(Article article);

        Optional<Article> findBySlug(String slug);

        Page<Article> paginateRecommendArticleList(String articleSlug);

        Page<Article> paginateAuthorArticleList(Long userId, Integer limit, Integer page, String sortBy,
                        String sortType,
                        String keyword);

        List<Article> findByIdIn(List<Long> ids);

        Page<Article> findArticles(ArticleParams params, boolean isAdmin);

        boolean deleteMultiple(List<Long> ids);

}
