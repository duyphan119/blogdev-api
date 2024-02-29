package com.api.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.article.Article;
import com.api.article.ArticleRepository;
import com.api.article.ArticleService;
import com.api.article_comment.ArticleComment;
import com.api.article_comment.ArticleCommentService;
import com.api.category.Category;
import com.api.category.CategoryRepository;
import com.api.utils.Helper;

@Service
public class WebService implements IWebService {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleCommentService articleCommentService;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private Helper helper;

    @Override
    public HomePageResponse findHomePageData() {
        List<Long> ids = new ArrayList<>();
        ids.add(Long.valueOf(-1));

        List<Article> totalArticles = getTodayArticles(ids);
        List<Article> mostRecentArticles = getMostRecentArticles(ids);
        List<Article> longreadsArticles = getLongreadsArticles(ids);
        List<CategoryArticleResponse> categoryArticles = getCategoryArticles(ids);
        List<Article> mostViewsArticles = getMostViewsArticles(ids);
        List<Article> mostCommentsArticles = getMostCommentsArticles(ids);
        List<Article> trendingArticles = getTrendingArticles(ids);

        return HomePageResponse.builder()
                .totalArticles(totalArticles)
                .mostRecentArticles(mostRecentArticles)
                .longreadsArticles(longreadsArticles)
                .categories(categoryArticles)
                .mostViewsArticles(mostViewsArticles)
                .mostCommentsArticles(mostCommentsArticles)
                .trendingArticles(trendingArticles)
                .build();
    }

    public List<Long> getIds(List<Article> articles) {
        return articles.stream()
                .map(Article::getId)
                .collect(Collectors.toList());
    }

    public List<Article> getTodayArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(3, 1, "id", "desc");

        Date now = new Date();
        Date startOfDay = DateUtils.truncate(now, java.util.Calendar.DAY_OF_MONTH);
        Date endOfDay = DateUtils.addMilliseconds(DateUtils.ceiling(now, java.util.Calendar.DAY_OF_MONTH), -1);

        Page<Article> articlePage = this.articleRepo.findByCreatedAtBetweenAndIdNotIn(startOfDay, endOfDay,
                ids,
                pageable);
        ids.addAll(getIds(articlePage.getContent()));
        if (articlePage.getContent().size() < 3) {
            Pageable pageable2 = helper.generatePageable(3, 1, "id", "desc");

            Page<Article> articlePage2 = this.articleRepo.findByIdNotIn(ids, pageable2);
            ids.addAll(getIds(articlePage2.getContent()));
            List<Article> newList = new ArrayList<>();
            newList.addAll(articlePage.getContent());
            newList.addAll(articlePage2.getContent());
            return newList;
        }

        return articlePage.getContent();
    }

    public List<Article> getMostRecentArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "id", "desc");

        Page<Article> articlePage = this.articleRepo.findByIdNotIn(ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return articlePage.getContent();
    }

    public List<Article> getLongreadsArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "id", "desc");

        Page<Article> articlePage = this.articleRepo.findByIsLongreadsAndIdNotIn(true, ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return articlePage.getContent();
    }

    public List<CategoryArticleResponse> getCategoryArticles(List<Long> ids) {
        Pageable pageable = helper.generatePageable(4, 1, "name", "desc");
        Pageable pageable2 = helper.generatePageable(3, 1, "id", "desc");

        Page<Category> categoryPage = this.categoryRepo.findAll(pageable);

        List<CategoryArticleResponse> categoryArticles = categoryPage.getContent().stream().map(category -> {
            return CategoryArticleResponse.builder().id(category.getId()).name(category.getName())
                    .slug(category.getSlug()).build();
        }).collect(Collectors.toList());

        for (int i = 0; i < categoryArticles.size(); i++) {
            Page<Article> articlePage = this.articleRepo
                    .findByIdNotInAndCategory_Id(ids, categoryArticles.get(i).getId(), pageable2);
            ids.addAll(getIds(articlePage.getContent()));
            categoryArticles.get(i).setArticles(
                    articlePage.getContent().stream().map(article -> {
                        return ArticleNoCategoryResponse.builder().id(article.getId()).title(article.getTitle())
                                .slug(article.getSlug()).imageUrl(article.getImageUrl()).build();
                    }).collect(Collectors.toList()));
        }

        return categoryArticles;
    }

    public List<Article> getMostViewsArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "views", "desc");

        Page<Article> articlePage = this.articleRepo.findByIdNotIn(ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return articlePage.getContent();
    }

    public List<Article> getMostCommentsArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "commentCount", "desc");

        Page<Article> articlePage = this.articleRepo.findByIdNotIn(ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return articlePage.getContent();
    }

    public List<Article> getTrendingArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(10, 1, "id", "desc");

        Page<Article> articlePage = this.articleRepo.findByViewsGreaterThanAndCommentCountGreaterThanAndIdNotIn(1001,
                10, ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return articlePage.getContent();
    }

    @Override
    public ArticleDetailPageResponse findArticleDetailPageData(String slug) {
        Optional<Article> articleOptional = this.articleRepo.findBySlug(slug);
        List<Long> ids = new ArrayList<>();
        ArticleDetailPageResponse articleDetailPageResponse = ArticleDetailPageResponse.builder().build();
        if (articleOptional.isPresent()) {
            articleDetailPageResponse
                    .setArticle(this.articleService.convertToArticleDetailResponse(articleOptional.get()));
            Pageable pageable = helper.generatePageable(8, 1, "createdAt", "desc");
            List<Article> recommendArticles = this.articleRepo
                    .findByIdNotAndCategory_Id(articleDetailPageResponse.getArticle().getId(),
                            articleDetailPageResponse.getArticle().getCategory().getId(), pageable)
                    .getContent();
            articleDetailPageResponse
                    .setRecommendArticles(recommendArticles);
            ids.add(articleDetailPageResponse.getArticle().getId());
            ids.addAll(this.getIds(recommendArticles));
            List<Article> mostRecentArticles = getMostRecentArticles(ids);
            List<Article> mostViewsArticles = getMostViewsArticles(ids);
            List<Article> mostCommentsArticles = getMostCommentsArticles(ids);
            List<Article> trendingArticles = getTrendingArticles(ids);
            articleDetailPageResponse.setMostRecentArticles(mostRecentArticles);
            articleDetailPageResponse.setMostViewsArticles(mostViewsArticles);
            articleDetailPageResponse.setMostCommentsArticles(mostCommentsArticles);
            articleDetailPageResponse.setTrendingArticles(trendingArticles);
        }
        Page<ArticleComment> articleCommentPage = this.articleCommentService.paginateByArticleSlug(slug, 5, 1,
                "createdAt", "desc", "");

        articleDetailPageResponse.setComments(articleCommentPage.getContent());
        articleDetailPageResponse.setCommentCount(articleCommentPage.getTotalElements());

        return articleDetailPageResponse;
    }

    @Override
    public ArticleListResponse findArticleListData(String articleSlug) {
        List<Long> ids = new ArrayList<>();
        ids.add(Long.valueOf(-1));

        if (!articleSlug.equals("")) {
            Optional<Article> articleOptional = this.articleRepo.findBySlug(articleSlug);
            if (articleOptional.isPresent()) {
                ids.add(articleOptional.get().getId());
            }
        }

        List<Article> mostRecentArticles = getMostRecentArticles(ids);
        List<Article> mostViewsArticles = getMostViewsArticles(ids);
        List<Article> mostCommentsArticles = getMostCommentsArticles(ids);
        List<Article> trendingArticles = getTrendingArticles(ids);

        return ArticleListResponse.builder().mostCommentsArticles(mostCommentsArticles)
                .mostRecentArticles(mostRecentArticles).mostViewsArticles(mostViewsArticles)
                .recommendArticles(trendingArticles).trendingArticles(trendingArticles).build();
    }

}
