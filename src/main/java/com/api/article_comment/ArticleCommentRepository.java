package com.api.article_comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    Page<ArticleComment> findByArticle_Slug(String articleSlug, Pageable pageable);

    List<ArticleComment> findByIdIn(List<Long> ids);
}
