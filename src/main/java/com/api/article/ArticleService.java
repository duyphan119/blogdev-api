package com.api.article;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.utils.Helper;

@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private Helper helper;

    @Override
    public Page<Article> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword) {
        Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

        return articleRepo.findAll(pageable);
    }

    @Override
    public Optional<Article> create(Article article) {
        try {
            return this.articleRepo.findById(articleRepo.save(article).getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String generateSlug(String name) {
        int randomNumber = helper.generateRandomNumber(1000);
        String slug = helper.generateSlug(name);
        while (true) {
            Optional<Article> articleOptional = articleRepo.findBySlug(slug + "-" + randomNumber);
            if (articleOptional.isEmpty()) {
                return slug + "-" + randomNumber;
            } else {
                randomNumber = helper.generateRandomNumber(1000);
            }
        }
    }

    @Override
    public ArticleResponse convertToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .author(article.getAuthor().getFullName())
                .categoryName(article.getCategory().getName())
                .categorySlug(article.getCategory().getSlug())
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .content(article.getContent())
                .imageUrl(article.getImageUrl())
                .introductionText(article.getIntroductionText())
                .createdAt(article.getCreatedAt())
                .isLongreads(article.getIsLongreads())
                .views(article.getViews())
                .build();
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        return this.articleRepo.findBySlug(slug);
    }

}
