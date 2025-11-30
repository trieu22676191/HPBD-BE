package com.birthday.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishDTO {
    private Long id;
    
    @NotBlank(message = "Lời chúc không được để trống")
    @Size(max = 500, message = "Lời chúc không được quá 500 ký tự")
    private String text;
    
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không được quá 100 ký tự")
    private String friendName;
    
    @NotBlank(message = "Biệt danh không được để trống")
    @Size(max = 100, message = "Biệt danh không được quá 100 ký tự")
    private String nickname;
    
    private Boolean isViewed = false;
}

