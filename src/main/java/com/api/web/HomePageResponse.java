package com.api.web;

import java.util.ArrayList;
import java.util.List;

import com.api.article.Article;
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
    private List<Article> totalArticles = new ArrayList<>();

    @JsonProperty("most_recent_articles")
    @Builder.Default
    private List<Article> mostRecentArticles = new ArrayList<>();

    @JsonProperty("longreads_articles")
    @Builder.Default
    private List<Article> longreadsArticles = new ArrayList<>();

    @JsonProperty("most_views_articles")
    @Builder.Default
    private List<Article> mostViewsArticles = new ArrayList<>();

    @JsonProperty("most_comments_articles")
    @Builder.Default
    private List<Article> mostCommentsArticles = new ArrayList<>();

    @Builder.Default
    private List<CategoryArticleResponse> categories = new ArrayList<>();

    @JsonProperty("trending_articles")
    @Builder.Default
    private List<Article> trendingArticles = new ArrayList<>();
}
