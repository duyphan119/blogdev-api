package com.api.article_tag;

import java.util.List;

import com.api.utils.ICrudService;

public interface IArticleTagService extends ICrudService<ArticleTag, Long> {
    String generateSlug(String name);

    List<ArticleTag> findByIdIn(List<Long> ids);
}
