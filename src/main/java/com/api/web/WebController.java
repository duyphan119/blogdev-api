package com.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;

@RestController
@RequestMapping("/web")
public class WebController {

    @Autowired
    private WebService webService;

    @GetMapping()
    public ResponseEntity<Object> getHomePageData() {
        return ResponseEntity.status(ApiConstant.STATUS_200).body(
                ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(webService.findHomePageData()).build());
    }

    @GetMapping("/article/slug/{slug}")
    public ResponseEntity<Object> getArticleDetailData(@PathVariable("slug") String slug) {
        return ResponseEntity.status(ApiConstant.STATUS_200).body(
                ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(webService.findArticleDetailPageData(slug))
                        .build());
    }
}
