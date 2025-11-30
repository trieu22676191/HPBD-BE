package com.birthday.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {
    private Long id;
    
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không được quá 200 ký tự")
    private String title;
    
    @NotBlank(message = "URL video không được để trống")
    private String url;
    
    private String type; // "video" hoặc "iframe"
    
    @Size(max = 500, message = "Mô tả không được quá 500 ký tự")
    private String description;
    
    private Boolean isLocked = false;
}

