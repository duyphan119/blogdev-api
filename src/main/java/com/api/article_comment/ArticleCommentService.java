package com.api.article_comment;

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
                    this.articleService.update(article);
                    articleCommentOptional.get().setArticle(article);
                }
            }

            return articleCommentOptional;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean delete(Long id) {
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

    @Override
    public Page<ArticleComment> paginateByArticleSlug(String articleSlug, Integer limit, Integer page, String sortBy,
            String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return this.articleCommentRepo.findByArticle_Slug(articleSlug, pageable);
    }

    @Override
    public Optional<ArticleComment> findById(Long id) {
        return this.articleCommentRepo.findById(id);
    }

    @Override
    public Optional<ArticleComment> update(ArticleComment articleComment) {

        try {
            Optional<ArticleComment> articleCommentOptional = Optional.of(this.articleCommentRepo.save(articleComment));

            if (articleCommentOptional.isPresent()) {
                Optional<Article> articleOptional = this.articleService.findById(articleComment.getArticle().getId());

                if (articleOptional.isPresent()) {
                    Article article = articleOptional.get();
                    article.setCommentCount(article.getCommentCount() + 1);
                    this.articleService.update(article);
                    articleCommentOptional.get().setArticle(article);
                }
            }

            return articleCommentOptional;
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
