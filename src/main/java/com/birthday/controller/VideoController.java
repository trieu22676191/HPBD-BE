package com.birthday.controller;

import com.birthday.dto.VideoDTO;
import com.birthday.service.VideoService;
import com.birthday.service.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
public class VideoController {
    
    @Autowired
    private VideoService videoService;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @GetMapping
    public ResponseEntity<List<VideoDTO>> getAllVideos() {
        List<VideoDTO> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }
    
    @PostMapping
    public ResponseEntity<VideoDTO> createVideo(@Valid @RequestBody VideoDTO videoDTO) {
        VideoDTO createdVideo = videoService.createVideo(videoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description) {
        try {
            // Kiểm tra file
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "File không được để trống"));
            }
            
            // Kiểm tra loại file
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "File phải là video"));
            }
            
            // Upload video lên Cloudinary
            String videoUrl = cloudinaryService.uploadVideo(file);
            
            // Tạo VideoDTO với URL từ Cloudinary
            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setUrl(videoUrl);
            videoDTO.setType("video");
            videoDTO.setTitle(title != null && !title.trim().isEmpty() 
                ? title.trim() 
                : (file.getOriginalFilename() != null 
                    ? file.getOriginalFilename().replaceAll("\\.[^.]*$", "") 
                    : "Video mới"));
            videoDTO.setDescription(description != null ? description.trim() : "Video đã tải lên");
            
            // Lưu vào database
            VideoDTO createdVideo = videoService.createVideo(videoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi upload video: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VideoDTO> updateVideo(
            @PathVariable Long id,
            @Valid @RequestBody VideoDTO videoDTO) {
        VideoDTO updatedVideo = videoService.updateVideo(id, videoDTO);
        return ResponseEntity.ok(updatedVideo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/toggle-lock")
    public ResponseEntity<VideoDTO> toggleLock(@PathVariable Long id) {
        VideoDTO updatedVideo = videoService.toggleLock(id);
        return ResponseEntity.ok(updatedVideo);
    }
}

