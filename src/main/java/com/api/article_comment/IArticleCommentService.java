package com.api.article_comment;

import java.util.List;

import org.springframework.data.domain.Page;

import com.api.utils.ICrudService;

public interface IArticleCommentService extends ICrudService<ArticleComment, Long> {
    Page<ArticleComment> paginateByArticleSlug(String articleSlug, Integer limit, Integer page, String sortBy,
            String sortType, String keyword);

    List<ArticleComment> findByIdIn(List<Long> ids);

}
