package com.birthday.service;

import com.birthday.dto.VideoDTO;
import com.birthday.entity.Video;
import com.birthday.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {
    
    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private CloudinaryService cloudinaryService;
    
    public List<VideoDTO> getAllVideos() {
        return videoRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public VideoDTO createVideo(VideoDTO videoDTO) {
        Video video = convertToEntity(videoDTO);
        Video savedVideo = videoRepository.save(video);
        return convertToDTO(savedVideo);
    }
    
    public VideoDTO updateVideo(Long id, VideoDTO videoDTO) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy video với ID: " + id));
        
        video.setTitle(videoDTO.getTitle());
        video.setUrl(videoDTO.getUrl());
        video.setType(videoDTO.getType());
        video.setDescription(videoDTO.getDescription());
        if (videoDTO.getIsLocked() != null) {
            video.setIsLocked(videoDTO.getIsLocked());
        }
        
        Video updatedVideo = videoRepository.save(video);
        return convertToDTO(updatedVideo);
    }
    
    public void deleteVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy video với ID: " + id));
        
        // Xóa video trên Cloudinary nếu URL là từ Cloudinary
        if (video.getUrl() != null && video.getUrl().contains("cloudinary.com")) {
            cloudinaryService.deleteVideo(video.getUrl());
        }
        
        videoRepository.deleteById(id);
    }
    
    private VideoDTO convertToDTO(Video video) {
        return new VideoDTO(
                video.getId(),
                video.getTitle(),
                video.getUrl(),
                video.getType(),
                video.getDescription(),
                video.getIsLocked() != null ? video.getIsLocked() : false
        );
    }
    
    private Video convertToEntity(VideoDTO videoDTO) {
        Video video = new Video();
        video.setTitle(videoDTO.getTitle());
        video.setUrl(videoDTO.getUrl());
        video.setType(videoDTO.getType() != null ? videoDTO.getType() : "video");
        video.setDescription(videoDTO.getDescription());
        video.setIsLocked(videoDTO.getIsLocked() != null ? videoDTO.getIsLocked() : false);
        return video;
    }
    
    public VideoDTO toggleLock(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy video với ID: " + id));
        
        video.setIsLocked(!video.getIsLocked());
        Video updatedVideo = videoRepository.save(video);
        return convertToDTO(updatedVideo);
    }
}

