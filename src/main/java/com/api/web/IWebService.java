package com.api.web;

public interface IWebService {
    HomePageResponse findHomePageData();

    ArticleListResponse findArticleListData(String articleSlug);
}
