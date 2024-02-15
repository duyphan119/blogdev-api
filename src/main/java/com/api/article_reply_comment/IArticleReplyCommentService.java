package com.api.article_reply_comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface IArticleReplyCommentService {
    Optional<ArticleReplyComment> create(ArticleReplyComment articleReplyComment);

    Page<ArticleReplyComment> paginateByArticleCommentId(Long articleCommentId, Integer limit, Integer page,
            String sortBy, String sortType, String keyword);

    List<ArticleReplyComment> findByArticleCommentId(Long articleCommentId);

    boolean delete(Long id);

    Optional<ArticleReplyComment> update(ArticleReplyComment articleReplyComment);

    Optional<ArticleReplyComment> findById(Long id);

}
