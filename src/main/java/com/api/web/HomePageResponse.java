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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePageResponse {
    @JsonProperty("today_articles")
    @Builder.Default
    List<ArticleResponse> totalArticles = new ArrayList<>();

    @JsonProperty("most_recent_articles")
    @Builder.Default
    List<ArticleResponse> mostRecentArticles = new ArrayList<>();

    @JsonProperty("longreads_articles")
    @Builder.Default
    List<ArticleResponse> longreadsArticles = new ArrayList<>();

    @JsonProperty("most_views_articles")
    @Builder.Default
    List<ArticleResponse> mostViewsArticles = new ArrayList<>();

    @JsonProperty("most_comments_articles")
    @Builder.Default
    List<ArticleResponse> mostCommentsArticles = new ArrayList<>();

    @Builder.Default
    List<CategoryArticleResponse> categories = new ArrayList<>();

    @JsonProperty("trending_articles")
    @Builder.Default
    List<ArticleResponse> trendingArticles = new ArrayList<>();
}
