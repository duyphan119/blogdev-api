package com.api.article;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.user.Author;
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

        return articleRepo.findByTitleIgnoreCaseContaining(keyword, pageable);
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
                .authorFullName(article.getAuthor().getFullName())
                .authorId(article.getAuthor().getId())
                .categoryName(article.getCategory().getName())
                .categorySlug(article.getCategory().getSlug())
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .imageUrl(article.getImageUrl())
                .introductionText(article.getIntroductionText())
                .createdAt(article.getCreatedAt())
                .isLongreads(article.getIsLongreads())
                .views(article.getViews())
                .isPublic(article.getIsPublic())
                .build();
    }

    @Override
    public ArticleDetailResponse convertToArticleDetailResponse(Article article) {
        return ArticleDetailResponse.builder()
                .author(Author.builder()
                        .email(article.getAuthor().getEmail())
                        .twitterUrl(article.getAuthor().getTwitterUrl())
                        .facebookUrl(article.getAuthor().getFacebookUrl())
                        .youtubeUrl(article.getAuthor().getYoutubeUrl())
                        .lastName(article.getAuthor().getLastName())
                        .linkedinUrl(article.getAuthor().getLinkedinUrl())
                        .githubUrl(article.getAuthor().getGithubUrl())
                        .firstName(article.getAuthor().getFirstName())
                        .fullName(article.getAuthor().getFullName())
                        .introduction(article.getAuthor().getIntroduction())
                        .id(article.getAuthor().getId())
                        .pinterestUrl(article.getAuthor().getPinterestUrl())
                        .imageUrl(article.getAuthor().getImageUrl())
                        .career(article.getAuthor().getCareer())
                        .build())
                .category(article.getCategory())
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .imageUrl(article.getImageUrl())
                .introductionText(article.getIntroductionText())
                .createdAt(article.getCreatedAt())
                .isLongreads(article.getIsLongreads())
                .content(article.getContent())
                .views(article.getViews())
                .isPublic(article.getIsPublic())
                .build();
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        return this.articleRepo.findBySlug(slug);
    }

    @Override
    public Optional<Article> update(Article article) {
        try {
            return this.articleRepo.findById(articleRepo.save(article).getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        return this.articleRepo.findById(id);
    }

    @Override
    public Page<Article> paginateRecommendArticleList(String articleSlug) {
        Optional<Article> articleOptional = this.articleRepo.findBySlug(articleSlug);
        Pageable pageable = helper.generatePageable(8, 1, "createdAt", "desc");
        if (articleOptional.isPresent()) {
            return this.articleRepo.findByIsPublicAndIdNotAndCategory_Id(true, articleOptional.get().getId(),
                    articleOptional.get().getCategory().getId(), pageable);
        }
        return Page.empty();

    }

    @Override
    public Page<Article> paginateAuthorArticleList(Long userId, Integer limit, Integer page, String sortBy,
            String sortType, String keyword) {
        return this.articleRepo.findByTitleIgnoreCaseContainingAndAuthor_Id(keyword, userId,
                helper.generatePageable(limit, page, sortBy, sortType));
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.articleRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
