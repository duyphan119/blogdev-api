package com.api.article;

import java.util.List;

import com.api.user.Author;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorArticlesResponse {
    private Author author;

    private List<Article> rows;

    @JsonProperty("total_pages")
    private Integer totalPages;

    private Long count;
}
