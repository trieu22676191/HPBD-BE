package com.birthday.controller;

import com.birthday.dto.PhotoDTO;
import com.birthday.service.PhotoService;
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
@RequestMapping("/api/photos")
@CrossOrigin(origins = "*")
public class PhotoController {
    
    @Autowired
    private PhotoService photoService;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    @GetMapping
    public ResponseEntity<List<PhotoDTO>> getAllPhotos() {
        List<PhotoDTO> photos = photoService.getAllPhotos();
        return ResponseEntity.ok(photos);
    }
    
    @PostMapping
    public ResponseEntity<PhotoDTO> createPhoto(@Valid @RequestBody PhotoDTO photoDTO) {
        PhotoDTO createdPhoto = photoService.createPhoto(photoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhoto);
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption) {
        try {
            // Kiểm tra file
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "File không được để trống"));
            }
            
            // Kiểm tra loại file
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "File phải là ảnh"));
            }
            
            // Upload ảnh lên Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file);
            
            // Tạo PhotoDTO với URL từ Cloudinary
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setUrl(imageUrl);
            photoDTO.setCaption(caption != null && !caption.trim().isEmpty() 
                ? caption.trim() 
                : (file.getOriginalFilename() != null 
                    ? file.getOriginalFilename().replaceAll("\\.[^.]*$", "") 
                    : "Ảnh mới"));
            
            // Lưu vào database
            PhotoDTO createdPhoto = photoService.createPhoto(photoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPhoto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Lỗi khi upload ảnh: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PhotoDTO> updatePhoto(
            @PathVariable Long id,
            @Valid @RequestBody PhotoDTO photoDTO) {
        PhotoDTO updatedPhoto = photoService.updatePhoto(id, photoDTO);
        return ResponseEntity.ok(updatedPhoto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/toggle-lock")
    public ResponseEntity<PhotoDTO> toggleLock(@PathVariable Long id) {
        PhotoDTO updatedPhoto = photoService.toggleLock(id);
        return ResponseEntity.ok(updatedPhoto);
    }
}

