package com.api.article;

import java.util.Date;

import com.api.category.Category;
import com.api.user.Author;
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
public class ArticleDetailResponse {
    private Long id;

    private String title;

    private String slug;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("introduction_text")
    private String introductionText;

    private String content;

    private Author author;

    @JsonProperty("created_at")
    private Date createdAt;

    private Category category;

    @JsonProperty("is_longreads")
    private Boolean isLongreads;

    private Long views;

    @JsonProperty("comment_count")
    private Integer commentCount;

}
