package com.api.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.article.Article;
import com.api.article.ArticleRepository;
import com.api.category.Category;
import com.api.category.CategoryRepository;
import com.api.utils.Helper;

@Service
public class WebService implements IWebService {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private Helper helper;

    @Override
    public HomePageResponse findHomePageData() {
        // articleRepo.deleteAll();
        // if (articleRepo.count() < 10) {
        // Random random = new Random();
        // List<String> imgs = new ArrayList<>();
        // imgs.add("https://i.pinimg.com/736x/46/56/13/465613044db2cb846d00f0de5e96e886.jpg");
        // imgs.add("https://i.pinimg.com/564x/8b/ce/0d/8bce0d9ed7b04a177447bfbbd9d23949.jpg");
        // imgs.add("https://i.pinimg.com/564x/1c/e8/8a/1ce88a276f3efd365a9fab52b716db13.jpg");
        // imgs.add("https://i.pinimg.com/564x/3a/7b/c4/3a7bc443fbb79fb5531181cb6c2c25bb.jpg");
        // imgs.add("https://i.pinimg.com/564x/15/e1/e9/15e1e9acd2a45906a44d8fe978ca43ce.jpg");

        // imgs.add("https://i.pinimg.com/736x/e3/4a/c3/e34ac3acd206c698cfe3f026320c209c.jpg");
        // imgs.add("https://i.pinimg.com/564x/63/10/f2/6310f28a92006b52dc511ee56f9b7611.jpg");
        // imgs.add("https://i.pinimg.com/564x/25/f0/f5/25f0f54015d6d5af27ebf65547c1d729.jpg");
        // imgs.add("https://i.pinimg.com/564x/22/6f/78/226f78af4591f1667a415da216fd435e.jpg");
        // imgs.add("https://i.pinimg.com/564x/3a/1e/e2/3a1ee20bf767aadedb3d9462ddec39c7.jpg");

        // imgs.add("https://i.pinimg.com/564x/5d/3c/25/5d3c2525d23356b5e10ec50002ffd6d7.jpg");
        // imgs.add("https://i.pinimg.com/564x/8a/cf/50/8acf5026fb8f49c84e2d45c5a95f86e7.jpg");
        // imgs.add("https://i.pinimg.com/564x/86/79/b5/8679b5cb9ef45a47665c069d07658633.jpg");
        // imgs.add("https://i.pinimg.com/564x/f0/01/04/f0010496234105ec922931849417d5d5.jpg");
        // imgs.add("https://i.pinimg.com/564x/40/dc/fe/40dcfee35894f3e021e93935c7c702bb.jpg");

        // imgs.add("https://i.pinimg.com/564x/b5/a8/02/b5a80268f27353d9cd9aef40e5aa75e2.jpg");
        // imgs.add("https://i.pinimg.com/564x/60/13/15/601315b9509d5bd6e4d3dfc0dd455c42.jpg");
        // imgs.add("https://i.pinimg.com/564x/1c/98/ba/1c98ba4b73f61f1de54d6c069d664c3a.jpg");
        // imgs.add("https://i.pinimg.com/564x/08/b9/7c/08b97c85e5794cda9da13fdc70577e5e.jpg");
        // imgs.add("https://i.pinimg.com/736x/4f/2e/43/4f2e43bddf00d7df655eac817223cef9.jpg");

        // imgs.add("https://i.pinimg.com/564x/74/57/4b/74574b6c664db357940f7d6bea57d53e.jpg");
        // imgs.add("https://i.pinimg.com/564x/0f/df/d1/0fdfd18d373df613cf140d6841bbff48.jpg");
        // imgs.add("https://i.pinimg.com/564x/bf/5c/f2/bf5cf21dd26994c80747ecc60fd38585.jpg");
        // imgs.add("https://i.pinimg.com/564x/85/70/68/857068aee5e60555b5ced8529b6d87c5.jpg");
        // imgs.add("https://i.pinimg.com/564x/0b/e9/4b/0be94baefeaf910d369749af91d8313d.jpg");

        // imgs.add("https://i.pinimg.com/564x/29/eb/8b/29eb8b5445b6eeb42bf312d89667a6fd.jpg");
        // imgs.add("https://i.pinimg.com/564x/a2/f3/ed/a2f3ed5595e1e5d77c073ca7674c2780.jpg");
        // imgs.add("https://i.pinimg.com/564x/78/44/54/7844542950fd5010f010c6a2e1fbe122.jpg");
        // imgs.add("https://i.pinimg.com/564x/f6/10/fe/f610feb7dda0168bb968a8830fd16b9c.jpg");
        // imgs.add("https://i.pinimg.com/564x/c3/1c/93/c31c93b0eb6043c77faa9c9a138a7ebc.jpg");

        // imgs.add("https://i.pinimg.com/564x/c3/48/0f/c3480f104e02bbb321e734b15f9a76ed.jpg");
        // imgs.add("https://i.pinimg.com/564x/b7/ea/7d/b7ea7d9b900c783e7c49b4c6581e9706.jpg");
        // imgs.add("https://i.pinimg.com/564x/4f/f4/58/4ff45876a2f904c7ea07b05bb69fbd8b.jpg");
        // imgs.add("https://i.pinimg.com/564x/51/2d/67/512d67e9becd47d946e6beec7f05f130.jpg");
        // imgs.add("https://i.pinimg.com/564x/b7/cc/e8/b7cce8e06f2d385acb9a230bb10df8fe.jpg");

        // imgs.add("https://i.pinimg.com/564x/2c/b2/71/2cb27185a026a7843c26c5d10cf9fd62.jpg");
        // imgs.add("https://i.pinimg.com/564x/b7/93/55/b7935567d38b7f618e0063ec07089771.jpg");
        // imgs.add("https://i.pinimg.com/736x/f5/ed/fe/f5edfeea6faf9d8008963f506226c259.jpg");
        // imgs.add("https://i.pinimg.com/736x/1b/d9/ae/1bd9aed968ba199914c8fba07b51dbe4.jpg");
        // imgs.add("https://i.pinimg.com/564x/3e/4e/22/3e4e225e04727e08a07149421c232753.jpg");

        // imgs.add("https://i.pinimg.com/736x/94/98/15/9498158b46ce0187a45d0bfebfcb4299.jpg");
        // imgs.add("https://i.pinimg.com/564x/0e/4f/ec/0e4fecf94a81806c0753e8a05f812fae.jpg");
        // imgs.add("https://i.pinimg.com/564x/24/85/bc/2485bc1b6f9162ae315ce1d633d47261.jpg");
        // imgs.add("https://i.pinimg.com/564x/d8/b7/bf/d8b7bf10000ce3ae5de4ac7c6c2c0b8e.jpg");
        // imgs.add("https://i.pinimg.com/564x/aa/91/65/aa9165cfb363d1150a1fefa789a77a54.jpg");
        // try {
        // // 4 cái trending
        // for (int i = 0; i < 4; i++) {
        // int indexImage = random.nextInt(imgs.size());
        // this.articleRepo.save(Article.builder()
        // .imageUrl(imgs.get(indexImage))
        // .title("Trending Post " + (4 - i))
        // .slug("trending-post-" + (4 - 1) + (200 - i))
        // .author(User.builder().id(Long.valueOf(1)).build())
        // .category(Category.builder().id(Long.valueOf(random.nextInt(6) + 1)).build())
        // .introductionText("Introduction text")
        // .content("Content")
        // .commentCount(11 + i)
        // .views(Long.valueOf(1001 + (i + 1)))
        // .build());
        // imgs.remove(indexImage);
        // }
        // // 5 cái most views
        // for (int i = 0; i < 5; i++) {
        // int indexImage = random.nextInt(imgs.size());
        // System.out.println(imgs.size());
        // this.articleRepo.save(Article.builder()
        // .imageUrl(imgs.get(indexImage))
        // .title("Most Views Post " + (5 - i))
        // .slug("most-views-post-" + (5 - 1) + (200 - i))
        // .author(User.builder().id(Long.valueOf(1)).build())
        // .category(Category.builder().id(Long.valueOf(random.nextInt(6) + 1)).build())
        // .introductionText("Introduction text")
        // .content("Content")
        // .commentCount(20 + i)
        // .views(Long.valueOf(100 * (i + 1)))
        // .build());
        // imgs.remove(indexImage);
        // }
        // // 5 cái most comments
        // for (int i = 0; i < 5; i++) {
        // int indexImage = random.nextInt(imgs.size());
        // System.out.println(imgs.size());
        // this.articleRepo.save(Article.builder()
        // .imageUrl(imgs.get(indexImage))
        // .title("Most Comments Post " + (5 - i))
        // .slug("most-comments-post-" + (5 - 1) + (200 - i))
        // .author(User.builder().id(Long.valueOf(1)).build())
        // .category(Category.builder().id(Long.valueOf(random.nextInt(6) + 1)).build())
        // .introductionText("Introduction text")
        // .content("Content")
        // .commentCount(1 + i)
        // .views(Long.valueOf(10000 * (i + 1)))
        // .build());
        // imgs.remove(indexImage);
        // }
        // // 5 cái Long reads
        // for (int i = 0; i < 5; i++) {
        // int indexImage = random.nextInt(imgs.size());
        // this.articleRepo.save(Article.builder()
        // .imageUrl(imgs.get(indexImage))
        // .title("Longreads Post " + (5 - i))
        // .slug("longreads-post-" + (5 - 1) + (200 - i))
        // .author(User.builder().id(Long.valueOf(1)).build())
        // .category(Category.builder().id(Long.valueOf(random.nextInt(6) + 1)).build())
        // .introductionText("Introduction text")
        // .content("Content")
        // .commentCount(1 + i)
        // .views(Long.valueOf(1 * (i + 1)))
        // .isLongreads(true)
        // .build());
        // imgs.remove(indexImage);
        // }
        // System.out.println("trước category --------" + imgs.size());
        // // 6 cái category
        // List<Category> categories = this.categoryRepo.findAll();
        // for (int i = 0; i < 6; i++) {
        // for (int j = 0; j < 3; j++) {
        // int indexImage = random.nextInt(imgs.size());
        // this.articleRepo.save(Article.builder()
        // .imageUrl(imgs.get(indexImage))
        // .title(categories.get(i).getName() + " Post " + (5 - j))
        // .slug(categories.get(i).getSlug() + "-post-" + (5 - 1) + (200 - j))
        // .author(User.builder().id(Long.valueOf(1)).build())
        // .category(categories.get(i))
        // .introductionText("Introduction text")
        // .content("Content")
        // .commentCount(1 + j)
        // .views(Long.valueOf(1 * (j + 1)))
        // .build());

        // imgs.remove(indexImage);
        // }
        // }
        // System.out.println("trước most recent --------" + imgs.size());
        // // 5 cái most recent
        // for (int i = 0; i < 5; i++) {
        // int indexImage = random.nextInt(imgs.size());
        // this.articleRepo.save(Article.builder()
        // .imageUrl(imgs.get(indexImage))
        // .title("Most Recent Post " + (5 - i))
        // .slug("most-recent-post-" + (5 - 1) + (200 - i))
        // .author(User.builder().id(Long.valueOf(1)).build())
        // .category(Category.builder().id(Long.valueOf(random.nextInt(6) + 1)).build())
        // .introductionText("Introduction text")
        // .content("Content")
        // .commentCount(1 + i)
        // .views(Long.valueOf(1 * (i + 1)))
        // .build());
        // imgs.remove(indexImage);
        // }
        // System.out.println("trước today --------" + imgs.size());
        // // 3 cái today
        // for (int i = 0; i < 3; i++) {
        // int indexImage = random.nextInt(imgs.size());

        // this.articleRepo.save(Article.builder()
        // .imageUrl(imgs.get(indexImage))
        // .title("Today Post " + (3 - i))
        // .slug("today-post-" + (3 - 1) + (200 - i))
        // .author(User.builder().id(Long.valueOf(1)).build())
        // .category(Category.builder().id(Long.valueOf(random.nextInt(6) + 1)).build())
        // .introductionText("Introduction text")
        // .content("Content")
        // .commentCount(1 + i)
        // .views(Long.valueOf(1 * (i + 1)))

        // .build());
        // imgs.remove(indexImage);
        // }
        // } catch (Exception e) {
        // System.out.println("Lỗi rồi --------" + e.getMessage());

        // }
        // }

        List<Long> ids = new ArrayList<>();
        ids.add(Long.valueOf(-1));

        List<ArticleResponse> totalArticles = getTodayArticles(ids);
        List<ArticleResponse> mostRecentArticles = getMostRecentArticles(ids);
        List<ArticleResponse> longreadsArticles = getLongreadsArticles(ids);
        List<CategoryArticleResponse> categoryArticles = getCategoryArticles(ids);
        List<ArticleResponse> mostViewsArticles = getMostViewsArticles(ids);
        List<ArticleResponse> mostCommentsArticles = getMostCommentsArticles(ids);
        List<ArticleResponse> trendingArticles = getTrendingArticles(ids);

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

    public List<ArticleResponse> convertArticleResponesList(List<Article> articles) {
        return articles.stream()
                .map(article -> {
                    return ArticleResponse.builder()
                            .author(article.getAuthor().getFullName())
                            .categoryName(article.getCategory().getName())
                            .categorySlug(article.getCategory().getSlug())
                            .id(article.getId())
                            .title(article.getTitle())
                            .slug(article.getSlug())
                            .imageUrl(article.getImageUrl())
                            .introductionText(article.getIntroductionText())
                            .createdAt(article.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ArticleResponse> getTodayArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(3, 1, "id", "desc");

        Date now = new Date();
        Date startOfDay = DateUtils.truncate(now, java.util.Calendar.DAY_OF_MONTH);
        Date endOfDay = DateUtils.addMilliseconds(DateUtils.ceiling(now, java.util.Calendar.DAY_OF_MONTH), -1);

        Page<Article> articlePage = this.articleRepo.findByCreatedAtBetweenAndIdNotIn(startOfDay, endOfDay,
                ids,
                pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return this.convertArticleResponesList(articlePage.getContent());
    }

    public List<ArticleResponse> getMostRecentArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "id", "desc");

        Page<Article> articlePage = this.articleRepo.findByIdNotIn(ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return this.convertArticleResponesList(articlePage.getContent());
    }

    public List<ArticleResponse> getLongreadsArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "id", "desc");

        Page<Article> articlePage = this.articleRepo.findByIsLongreadsAndIdNotIn(true, ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return this.convertArticleResponesList(articlePage.getContent());
    }

    public List<CategoryArticleResponse> getCategoryArticles(List<Long> ids) {
        Pageable pageable = helper.generatePageable(6, 1, "name", "desc");
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

    public List<ArticleResponse> getMostViewsArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "views", "desc");

        Page<Article> articlePage = this.articleRepo.findByIdNotIn(ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return this.convertArticleResponesList(articlePage.getContent());
    }

    public List<ArticleResponse> getMostCommentsArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(5, 1, "commentCount", "desc");

        Page<Article> articlePage = this.articleRepo.findByIdNotIn(ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return this.convertArticleResponesList(articlePage.getContent());
    }

    public List<ArticleResponse> getTrendingArticles(List<Long> ids) {

        Pageable pageable = helper.generatePageable(10, 1, "id", "desc");

        Page<Article> articlePage = this.articleRepo.findByViewsGreaterThanAndCommentCountGreaterThanAndIdNotIn(1001,
                10, ids, pageable);
        ids.addAll(getIds(articlePage.getContent()));
        return this.convertArticleResponesList(articlePage.getContent());
    }

}
