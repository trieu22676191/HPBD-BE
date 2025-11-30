package com.birthday.service;

import com.birthday.dto.WishDTO;
import com.birthday.entity.Wish;
import com.birthday.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {
    
    @Autowired
    private WishRepository wishRepository;
    
    public List<WishDTO> getAllWishes() {
        return wishRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public WishDTO createWish(WishDTO wishDTO) {
        Wish wish = convertToEntity(wishDTO);
        Wish savedWish = wishRepository.save(wish);
        return convertToDTO(savedWish);
    }
    
    public WishDTO updateWish(Long id, WishDTO wishDTO) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lời chúc với ID: " + id));
        
        wish.setText(wishDTO.getText());
        wish.setFriendName(wishDTO.getFriendName());
        wish.setNickname(wishDTO.getNickname());
        if (wishDTO.getIsViewed() != null) {
            wish.setIsViewed(wishDTO.getIsViewed());
        }
        
        Wish updatedWish = wishRepository.save(wish);
        return convertToDTO(updatedWish);
    }
    
    public void deleteWish(Long id) {
        if (!wishRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy lời chúc với ID: " + id);
        }
        wishRepository.deleteById(id);
    }
    
    public WishDTO markAsViewed(Long id) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lời chúc với ID: " + id));
        
        wish.setIsViewed(true);
        Wish updatedWish = wishRepository.save(wish);
        return convertToDTO(updatedWish);
    }
    
    private WishDTO convertToDTO(Wish wish) {
        return new WishDTO(
                wish.getId(),
                wish.getText(),
                wish.getFriendName(),
                wish.getNickname(),
                wish.getIsViewed() != null ? wish.getIsViewed() : false
        );
    }
    
    private Wish convertToEntity(WishDTO wishDTO) {
        Wish wish = new Wish();
        wish.setText(wishDTO.getText());
        wish.setFriendName(wishDTO.getFriendName());
        wish.setNickname(wishDTO.getNickname());
        wish.setIsViewed(wishDTO.getIsViewed() != null ? wishDTO.getIsViewed() : false);
        return wish;
    }
}

