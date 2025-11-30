package com.birthday.controller;

import com.birthday.dto.WishDTO;
import com.birthday.service.WishService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishes")
@CrossOrigin(origins = "*")
public class WishController {
    
    @Autowired
    private WishService wishService;
    
    @GetMapping
    public ResponseEntity<List<WishDTO>> getAllWishes() {
        List<WishDTO> wishes = wishService.getAllWishes();
        return ResponseEntity.ok(wishes);
    }
    
    @PostMapping
    public ResponseEntity<WishDTO> createWish(@Valid @RequestBody WishDTO wishDTO) {
        WishDTO createdWish = wishService.createWish(wishDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWish);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<WishDTO> updateWish(
            @PathVariable Long id,
            @Valid @RequestBody WishDTO wishDTO) {
        WishDTO updatedWish = wishService.updateWish(id, wishDTO);
        return ResponseEntity.ok(updatedWish);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id) {
        wishService.deleteWish(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/mark-viewed")
    public ResponseEntity<WishDTO> markAsViewed(@PathVariable Long id) {
        WishDTO updatedWish = wishService.markAsViewed(id);
        return ResponseEntity.ok(updatedWish);
    }
}

