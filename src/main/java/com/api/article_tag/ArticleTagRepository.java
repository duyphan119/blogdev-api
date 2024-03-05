package com.api.article_tag;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    Optional<ArticleTag> findBySlug(String slug);

    Page<ArticleTag> findByNameIgnoreCaseContaining(String name, Pageable pageable);

    List<ArticleTag> findByIdIn(List<Long> ids);
}
