package com.birthday.service;

import com.birthday.dto.PhotoDTO;
import com.birthday.entity.Photo;
import com.birthday.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    public List<PhotoDTO> getAllPhotos() {
        return photoRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PhotoDTO createPhoto(PhotoDTO photoDTO) {
        Photo photo = convertToEntity(photoDTO);
        Photo savedPhoto = photoRepository.save(photo);
        return convertToDTO(savedPhoto);
    }
    
    public PhotoDTO updatePhoto(Long id, PhotoDTO photoDTO) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh với ID: " + id));
        
        photo.setUrl(photoDTO.getUrl());
        photo.setCaption(photoDTO.getCaption());
        
        Photo updatedPhoto = photoRepository.save(photo);
        return convertToDTO(updatedPhoto);
    }
    
    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh với ID: " + id));
        
        // Xóa ảnh trên Cloudinary nếu URL là từ Cloudinary
        if (photo.getUrl() != null && photo.getUrl().contains("cloudinary.com")) {
            cloudinaryService.deleteImage(photo.getUrl());
        }
        
        photoRepository.deleteById(id);
    }
    
    private PhotoDTO convertToDTO(Photo photo) {
        return new PhotoDTO(
                photo.getId(),
                photo.getUrl(),
                photo.getCaption(),
                photo.getIsLocked() != null ? photo.getIsLocked() : false
        );
    }
    
    private Photo convertToEntity(PhotoDTO photoDTO) {
        Photo photo = new Photo();
        photo.setUrl(photoDTO.getUrl());
        photo.setCaption(photoDTO.getCaption());
        photo.setIsLocked(photoDTO.getIsLocked() != null ? photoDTO.getIsLocked() : false);
        return photo;
    }
    
    public PhotoDTO toggleLock(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh với ID: " + id));
        
        photo.setIsLocked(!photo.getIsLocked());
        Photo updatedPhoto = photoRepository.save(photo);
        return convertToDTO(updatedPhoto);
    }
}

