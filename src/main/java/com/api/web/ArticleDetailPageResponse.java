package com.api.web;

import java.util.ArrayList;
import java.util.List;

import com.api.article.ArticleDetailResponse;
import com.api.article.Article;
import com.api.article_comment.ArticleComment;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDetailPageResponse {
    private ArticleDetailResponse article;

    private List<ArticleComment> comments;

    @JsonProperty("comment_count")
    @Builder.Default
    private Long commentCount = Long.valueOf(0);

    @JsonProperty("recommend_articles")
    @Builder.Default
    private List<Article> recommendArticles = new ArrayList<>();

    @JsonProperty("most_recent_articles")
    @Builder.Default
    private List<Article> mostRecentArticles = new ArrayList<>();

    @JsonProperty("most_views_articles")
    @Builder.Default
    private List<Article> mostViewsArticles = new ArrayList<>();

    @JsonProperty("most_comments_articles")
    @Builder.Default
    private List<Article> mostCommentsArticles = new ArrayList<>();

    @JsonProperty("trending_articles")
    @Builder.Default
    private List<Article> trendingArticles = new ArrayList<>();
}
