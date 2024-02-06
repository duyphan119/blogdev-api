package com.api.upload;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
    public String uploadImageSingle(MultipartFile file);
}
