package com.api.article;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
        Optional<Article> findBySlug(String slug);

        Page<Article> findByCreatedAtBetweenAndIdNotIn(Date startOfDay, Date endOfDay, List<Long> ids,
                        Pageable pageable);

        Page<Article> findByIdNotIn(List<Long> ids, Pageable pageable);

        Page<Article> findByIsLongreadsAndIdNotIn(Boolean isLongreads, List<Long> ids, Pageable pageable);

        Page<Article> findByIdNotInAndCategory_Id(List<Long> ids, Long categoryId, Pageable pageable);

        Page<Article> findByViewsGreaterThanAndCommentCountGreaterThanAndIdNotIn(Integer views, Integer commentCount,
                        List<Long> ids, Pageable pageable);

        Page<Article> findByIdNotAndCategory_Id(Long id, Long categoryId, Pageable pageable);

}
