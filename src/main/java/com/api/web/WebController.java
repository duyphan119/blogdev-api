package com.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        // lấy 3 bài hôm nay
        // 5 bài gần nhất
        // 5 bài có nội dung nhiều
        // 6 category, mỗi category có 3 bài
        // 5 bài nhiều view
        // 5 bài nhiều comment
        // 4 bài trending (nhiều comment + nhiều view + gần đây)
        return ResponseEntity.status(ApiConstant.STATUS_200).body(
                ApiResponse.builder().message(ApiConstant.MSG_SUCCESS).data(webService.findHomePageData()).build());
    }
}
