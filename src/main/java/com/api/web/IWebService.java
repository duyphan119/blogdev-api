package com.api.web;

public interface IWebService {
    HomePageResponse findHomePageData();

    ArticleDetailPageResponse findArticleDetailPageData(String slug);
}
