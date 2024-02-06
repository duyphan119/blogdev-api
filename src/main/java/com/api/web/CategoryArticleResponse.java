package com.api.web;

import java.util.List;

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
public class CategoryArticleResponse {
    private Long id;
    private String name;
    private String slug;
    private List<ArticleNoCategoryResponse> articles;
}
