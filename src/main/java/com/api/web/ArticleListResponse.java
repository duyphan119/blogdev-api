package com.api.web;

import java.util.ArrayList;
import java.util.List;

import com.api.article.ArticleResponse;
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
public class ArticleListResponse {
    @JsonProperty("recommend_articles")
    @Builder.Default
    private List<ArticleResponse> recommendArticles = new ArrayList<>();

    @JsonProperty("most_recent_articles")
    @Builder.Default
    private List<ArticleResponse> mostRecentArticles = new ArrayList<>();

    @JsonProperty("most_views_articles")
    @Builder.Default
    private List<ArticleResponse> mostViewsArticles = new ArrayList<>();

    @JsonProperty("most_comments_articles")
    @Builder.Default
    private List<ArticleResponse> mostCommentsArticles = new ArrayList<>();

    @JsonProperty("trending_articles")
    @Builder.Default
    private List<ArticleResponse> trendingArticles = new ArrayList<>();
}
