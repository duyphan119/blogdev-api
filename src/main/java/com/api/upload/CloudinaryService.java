package com.api.upload;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService implements ICloudinaryService {
    private final Cloudinary cloudinary;
    private final String folder;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret,
            @Value("${cloudinary.folder}") String folder) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
        this.folder = folder;
    }

    @Override
    public String uploadImageSingle(MultipartFile file) {
        try {
            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder));
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            return null;
        }
    }

    public String extractPublicId(String secureUrl) {
        String[] splittedUrl = secureUrl.split(folder);
        if (splittedUrl.length > 0) {
            String str = splittedUrl[splittedUrl.length - 1];
            String[] splittedStr = str.split("\\.");
            if (splittedStr.length > 0) {
                return folder + splittedStr[0];
            }
        }

        return "";
    }

    @Override
    public boolean deleteImage(String url) {
        try {
            String publicId = this.extractPublicId(url);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
}
