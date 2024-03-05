package com.api.category_parent;

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
public class CategoryParentParams {
    private Integer pageSize;
    private Integer page;
    private String sortBy;
    private String sortType;
    private String keyword;
}
