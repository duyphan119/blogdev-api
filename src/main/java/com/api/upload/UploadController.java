package com.api.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.utils.ApiConstant;
import com.api.utils.ApiResponse;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/image/single")
    public ResponseEntity<Object> uploadSingle(@RequestParam("image") MultipartFile image) {
        String imageUrl = cloudinaryService.uploadImageSingle(image);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message(imageUrl == null ? ApiConstant.MSG_ERROR : ApiConstant.MSG_SUCCESS).data(imageUrl).build());
    }
}
