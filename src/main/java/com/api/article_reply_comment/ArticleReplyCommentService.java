package com.api.article_reply_comment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.utils.Helper;

@Service
public class ArticleReplyCommentService implements IArticleReplyCommentService {

    @Autowired
    private ArticleReplyCommentRepository articleReplyCommentRepo;

    @Autowired
    private Helper helper;

    @Override
    public Optional<ArticleReplyComment> create(ArticleReplyComment articleReplyComment) {
        try {
            return Optional.of(this.articleReplyCommentRepo.save(articleReplyComment));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<ArticleReplyComment> paginateByArticleCommentId(Long articleCommentId, Integer limit, Integer page,
            String sortBy, String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return this.articleReplyCommentRepo.findByArticleComment_Id(articleCommentId, pageable);
    }

    @Override
    public List<ArticleReplyComment> findByArticleCommentId(Long articleCommentId) {
        return this.articleReplyCommentRepo.findByArticleComment_Id(articleCommentId);
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.articleReplyCommentRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<ArticleReplyComment> update(ArticleReplyComment articleReplyComment) {
        try {
            return Optional.of(this.articleReplyCommentRepo.save(articleReplyComment));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ArticleReplyComment> findById(Long id) {
        return this.articleReplyCommentRepo.findById(id);
    }

}
