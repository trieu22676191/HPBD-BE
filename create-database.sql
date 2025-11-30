-- ============================================
-- TẠO DATABASE VÀ CÁC BẢNG CHO HPBD BACKEND
-- ============================================

-- Tạo database
CREATE DATABASE IF NOT EXISTS birthday_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Sử dụng database
USE birthday_db;

-- ============================================
-- BẢNG WISHES (Lời Chúc)
-- ============================================
CREATE TABLE IF NOT EXISTS wishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(500) NOT NULL COMMENT 'Nội dung lời chúc',
    friend_name VARCHAR(100) NOT NULL COMMENT 'Tên người gửi',
    nickname VARCHAR(100) NOT NULL COMMENT 'Biệt danh',
    is_viewed BOOLEAN DEFAULT FALSE COMMENT 'Trạng thái đã xem',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    INDEX idx_created_at (created_at),
    INDEX idx_is_viewed (is_viewed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- BẢNG PHOTOS (Ảnh)
-- ============================================
CREATE TABLE IF NOT EXISTS photos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url LONGTEXT NOT NULL COMMENT 'URL hoặc base64 của ảnh',
    caption VARCHAR(200) NOT NULL COMMENT 'Tiêu đề ảnh',
    is_locked BOOLEAN DEFAULT FALSE COMMENT 'Trạng thái khóa ảnh',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    INDEX idx_created_at (created_at),
    INDEX idx_is_locked (is_locked)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- BẢNG VIDEOS (Video)
-- ============================================
CREATE TABLE IF NOT EXISTS videos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT 'Tiêu đề video',
    url LONGTEXT NOT NULL COMMENT 'URL video hoặc embed URL hoặc base64',
    type VARCHAR(50) DEFAULT 'video' COMMENT 'Loại: video hoặc iframe',
    description VARCHAR(500) COMMENT 'Mô tả video',
    is_locked BOOLEAN DEFAULT FALSE COMMENT 'Trạng thái khóa video',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    INDEX idx_created_at (created_at),
    INDEX idx_type (type),
    INDEX idx_is_locked (is_locked)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
USE birthday_db;

-- ===============================
-- INSERT PHOTOS
-- ===============================

INSERT INTO photos (url, caption) VALUES
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447253/h9_dfyqbj.jpg',
 'Khoảnh khắc dễ thương trong nắng sớm'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447253/h6_cqtydj.jpg',
 'Một chút dịu dàng giữa không gian quen thuộc'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447252/h8_ptxsrg.jpg',
 'Ánh mắt thật hiền – một khung hình rất đẹp'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447238/h3_oqujlt.jpg',
 'Ngồi xuống một chút, để lưu lại một bức ảnh thật xinh'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447238/h10_l9a3bq.jpg',
 'Nét cổ điển pha chút tinh tế, nổi bật giữa không gian'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447227/h2_lfdzuw.jpg',
 'Tự nhiên và nhẹ nhàng – vẻ đẹp đúng khoảnh khắc'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447226/h7_tbphpf.jpg',
 'Khung cảnh ấm áp và tràn đầy cảm xúc'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447220/h4_taycha.jpg',
 'Một góc ảnh rất nghệ – cảm giác bình yên'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447215/h1_s6nvk4.jpg',
 'Nụ cười thật tươi – khoảnh khắc đáng yêu'),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447214/h5_ilklny.jpg',
 'Bức ảnh ghi lại một buổi tối thật đẹp');

-- ===============================
-- INSERT VIDEOS
-- ===============================

INSERT INTO videos (title, url, type, description) VALUES 
(
  'Khoảnh khắc đáng nhớ',
  'https://res.cloudinary.com/dclomtdc3/video/upload/v1764446593/6578534275909_wwsuzb.mp4',
  'video',
  'Một đoạn video ngắn nhưng tràn đầy kỷ niệm đẹp.'
);


-- ============================================
INSERT INTO wishes (text, friend_name, nickname) VALUES 
(
  'Chúc em sinh nhật vui vẻ! Chúc em luôn xinh đẹp, hạnh phúc và gặp thật nhiều điều tốt đẹp trong cuộc sống ❤️',
  'Triệu',
  'Khỉ già'
);


