package com.api.web;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;

    private String title;

    private String slug;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("introduction_text")
    private String introductionText;

    private String author;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_slug")
    private String categorySlug;
}
