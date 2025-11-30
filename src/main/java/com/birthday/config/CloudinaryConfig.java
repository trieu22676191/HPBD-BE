package com.birthday.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class CloudinaryConfig {
    
    @Value("${cloudinary.cloud-name:}")
    private String cloudName;
    
    @Value("${cloudinary.api-key:}")
    private String apiKey;
    
    @Value("${cloudinary.api-secret:}")
    private String apiSecret;
    
    @Bean
    public Cloudinary cloudinary() {
        // Kiểm tra xem Cloudinary đã được cấu hình chưa
        if (!StringUtils.hasText(cloudName) || 
            !StringUtils.hasText(apiKey) || 
            !StringUtils.hasText(apiSecret)) {
            throw new IllegalStateException(
                "Cloudinary chưa được cấu hình. Vui lòng cập nhật application.properties với " +
                "cloudinary.cloud-name, cloudinary.api-key, và cloudinary.api-secret"
            );
        }
        
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }
}

