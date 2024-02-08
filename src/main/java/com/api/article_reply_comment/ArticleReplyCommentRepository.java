package com.api.article_reply_comment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleReplyCommentRepository extends JpaRepository<ArticleReplyComment, Long> {
    Page<ArticleReplyComment> findByArticleComment_Id(Long articleCommentId, Pageable pageable);

    List<ArticleReplyComment> findByArticleComment_Id(Long articleCommentId);
}
