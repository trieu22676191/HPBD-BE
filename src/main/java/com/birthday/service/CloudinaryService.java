package com.birthday.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    
    @Autowired
    private Cloudinary cloudinary;
    
    /**
     * Upload ảnh lên Cloudinary
     * @param file File ảnh cần upload
     * @return URL của ảnh trên Cloudinary
     * @throws IOException Nếu có lỗi khi upload
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }
        
        // Upload lên Cloudinary với transformation đơn giản
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "folder", "hpbd-photos", // Thư mục trên Cloudinary
                "resource_type", "image",
                "width", 1920,
                "height", 1920,
                "crop", "limit",
                "quality", "auto:good"
            )
        );
        
        // Trả về URL của ảnh
        return (String) uploadResult.get("secure_url");
    }
    
    /**
     * Upload video lên Cloudinary
     * @param file File video cần upload
     * @return URL của video trên Cloudinary
     * @throws IOException Nếu có lỗi khi upload
     */
    public String uploadVideo(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }
        
        // Upload lên Cloudinary
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "folder", "hpbd-videos", // Thư mục trên Cloudinary
                "resource_type", "video",
                "quality", "auto:good"
            )
        );
        
        // Trả về URL của video
        return (String) uploadResult.get("secure_url");
    }
    
    /**
     * Xóa ảnh khỏi Cloudinary
     * @param imageUrl URL của ảnh cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteImage(String imageUrl) {
        try {
            // Lấy public_id từ URL
            String publicId = extractPublicId(imageUrl);
            if (publicId == null) {
                return false;
            }
            
            // Xóa ảnh
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa ảnh từ Cloudinary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Xóa video khỏi Cloudinary
     * @param videoUrl URL của video cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteVideo(String videoUrl) {
        try {
            // Lấy public_id từ URL
            String publicId = extractPublicId(videoUrl);
            if (publicId == null) {
                return false;
            }
            
            // Xóa video (cần chỉ định resource_type = "video")
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "video"));
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa video từ Cloudinary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Trích xuất public_id từ URL Cloudinary
     * @param imageUrl URL của ảnh/video
     * @return public_id hoặc null nếu không tìm thấy
     */
    private String extractPublicId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }
        
        try {
            // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/{version}/{folder}/{filename}.{format}
            // Hoặc: https://res.cloudinary.com/{cloud_name}/image/upload/{folder}/{filename}.{format}
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }
            
            String path = parts[1];
            
            // Loại bỏ version nếu có (format: v1234567890/...)
            if (path.matches("^v\\d+/.*")) {
                path = path.substring(path.indexOf("/") + 1);
            }
            
            // Loại bỏ extension
            int lastDot = path.lastIndexOf(".");
            if (lastDot > 0) {
                path = path.substring(0, lastDot);
            }
            
            return path;
        } catch (Exception e) {
            return null;
        }
    }
}

