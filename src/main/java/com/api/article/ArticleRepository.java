package com.api.article;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
        Optional<Article> findBySlug(String slug);

        Page<Article> findByTitleIgnoreCaseContaining(String keyword, Pageable pageable);

        Page<Article> findByCreatedAtBetweenAndIdNotIn(Date startOfDay, Date endOfDay, List<Long> ids,
                        Pageable pageable);

        Page<Article> findByIdNotIn(List<Long> ids, Pageable pageable);

        Page<Article> findByIsLongreadsAndIdNotIn(Boolean isLongreads, List<Long> ids, Pageable pageable);

        Page<Article> findByIdNotInAndCategory_Id(List<Long> ids, Long categoryId, Pageable pageable);

        Page<Article> findByViewsGreaterThanAndCommentCountGreaterThanAndIdNotIn(Integer views, Integer commentCount,
                        List<Long> ids, Pageable pageable);

        Page<Article> findByIdNotAndCategory_Id(Long id, Long categoryId, Pageable pageable);

        Page<Article> findByAuthor_Id(Long userId, Pageable pageable);

        Page<Article> findByTitleIgnoreCaseContainingAndAuthor_Id(String title,
                        Long userId,
                        Pageable pageable);

        Optional<Article> findByApprovedAndIsPublicAndSlug(Boolean approved, Boolean isPublic, String slug);

        Page<Article> findByApprovedAndIsPublicAndCreatedAtBetweenAndIdNotIn(Boolean approved, Boolean isPublic,
                        Date startOfDay, Date endOfDay,
                        List<Long> ids,
                        Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndIdNotIn(Boolean approved, Boolean isPublic, List<Long> ids,
                        Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndIsLongreadsAndIdNotIn(Boolean approved, Boolean isPublic,
                        Boolean isLongreads, List<Long> ids,
                        Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndIdNotInAndCategory_Id(Boolean approved, Boolean isPublic,
                        List<Long> ids, Long categoryId,
                        Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndViewsGreaterThanAndCommentCountGreaterThanAndIdNotIn(Boolean approved,
                        Boolean isPublic,
                        Integer views, Integer commentCount,
                        List<Long> ids, Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndIdNotAndCategory_Id(Boolean approved, Boolean isPublic, Long id,
                        Long categoryId,
                        Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndAuthor_Id(Boolean approved, Boolean isPublic, Long userId,
                        Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndCategory_Slug(Boolean approved, Boolean isPublic, String categorySlug,
                        Pageable pageable);

        List<Article> findByIdIn(List<Long> ids);

        Page<Article> findByCategory_Slug(String categorySlug,
                        Pageable pageable);

        Page<Article> findByApprovedAndIsPublicAndTitleIgnoreCaseContaining(Boolean approved, Boolean isPublic,
                        String keyword, Pageable pageable);
}
