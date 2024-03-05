package com.api.article_comment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.article.Article;
import com.api.article.ArticleService;
import com.api.utils.Helper;

@Service
public class ArticleCommentService implements IArticleCommentService {

    @Autowired
    private ArticleCommentRepository articleCommentRepo;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Helper helper;

    @Override
    public Optional<ArticleComment> create(ArticleComment articleComment) {

        try {
            Optional<ArticleComment> articleCommentOptional = Optional.of(this.articleCommentRepo.save(articleComment));

            if (articleCommentOptional.isPresent()) {
                Optional<Article> articleOptional = this.articleService.findById(articleComment.getArticle().getId());

                if (articleOptional.isPresent()) {
                    Article article = articleOptional.get();
                    article.setCommentCount(article.getCommentCount() + 1);
                    this.articleService.update(article.getId(), article);
                    articleCommentOptional.get().setArticle(article);
                }
            }

            return articleCommentOptional;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.articleCommentRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Page<ArticleComment> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return this.articleCommentRepo.findAll(pageable);
    }

    // @Override
    // public Page<ArticleComment> paginateByArticleSlug(String articleSlug, int
    // limit, int page, String sortBy,
    // String sortType, String keyword) {
    // Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

    // return this.articleCommentRepo.findByArticle_Slug(articleSlug, pageable);
    // }

    @Override
    public Optional<ArticleComment> findById(Long id) {
        return this.articleCommentRepo.findById(id);
    }

    @Override
    public Optional<ArticleComment> update(Long id, ArticleComment body) {

        try {
            Optional<ArticleComment> articleCommentOptional = this.articleCommentRepo.findById(id);
            if (articleCommentOptional.isPresent()) {
                body.setId(id);
                articleCommentOptional = Optional.of(this.articleCommentRepo.save(body));

                if (articleCommentOptional.isPresent()) {
                    Optional<Article> articleOptional = this.articleService.findById(body.getArticle().getId());

                    if (articleOptional.isPresent()) {
                        Article article = articleOptional.get();
                        article.setCommentCount(article.getCommentCount() + 1);
                        this.articleService.update(article.getId(), article);
                        articleCommentOptional.get().setArticle(article);
                    }
                }

                return articleCommentOptional;
            }

        } catch (Exception e) {
        }
        return Optional.empty();
    }

    @Override
    public Page<ArticleComment> paginateByArticleSlug(String articleSlug, Integer limit, Integer page, String sortBy,
            String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return this.articleCommentRepo.findByArticle_Slug(articleSlug, pageable);
    }

    @Override
    public boolean deleteMultiple(List<Long> ids) {
        try {
            this.articleCommentRepo.deleteAllByIdInBatch(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ArticleComment> findByIdIn(List<Long> ids) {
        return this.articleCommentRepo.findByIdIn(ids);
    }
}
