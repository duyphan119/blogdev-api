package com.api.article_tag;

import com.api.utils.ICrudService;

public interface IArticleTagService extends ICrudService<ArticleTag, Long> {
    String generateSlug(String name);

}
