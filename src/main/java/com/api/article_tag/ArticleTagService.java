package com.api.article_tag;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.utils.Helper;

@Service
public class ArticleTagService implements IArticleTagService {

    @Autowired
    private ArticleTagRepository articleTagRepo;

    @Autowired
    private Helper helper;

    @Override
    public Optional<ArticleTag> findById(Long id) {
        try {
            return this.articleTagRepo.findById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<ArticleTag> create(ArticleTag body) {
        try {
            return Optional.of(this.articleTagRepo.save(body));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<ArticleTag> update(Long id, ArticleTag body) {
        try {
            Optional<ArticleTag> articleTagOptional = this.articleTagRepo.findById(id);

            if (articleTagOptional.isPresent()) {
                body.setId(id);
                return Optional.of(this.articleTagRepo.save(body));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.articleTagRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public Page<ArticleTag> paginate(Integer limit, Integer page, String sortBy, String sortType, String keyword) {
        try {
            Pageable pageable = helper.generatePageable(limit, page, sortBy, sortType);

            if (keyword != null && !keyword.equals("")) {
                return this.articleTagRepo.findByNameIgnoreCaseContaining(keyword, pageable);
            }

            return this.articleTagRepo.findAll(pageable);

        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public String generateSlug(String name) {
        int randomNumber = helper.generateRandomNumber(1000);
        String slug = helper.generateSlug(name);
        while (true) {
            Optional<ArticleTag> articleOptional = this.articleTagRepo.findBySlug(slug + "-" + randomNumber);
            if (articleOptional.isEmpty()) {
                return slug + "-" + randomNumber;
            } else {
                randomNumber = helper.generateRandomNumber(1000);
            }
        }
    }

}
