package com.api.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryParams {
    private Integer pageSize;
    private Integer page;
    private String sortBy;
    private String sortType;
    private String categoryParentSlug;
    private String keyword;
}
