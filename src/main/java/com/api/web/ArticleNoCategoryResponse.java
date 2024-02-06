package com.api.web;

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
public class ArticleNoCategoryResponse {
    private Long id;
    private String title;
    private String slug;
    @JsonProperty("image_url")
    private String imageUrl;
}
