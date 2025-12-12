-- ============================================
-- INSERT SAMPLE DATA VÀO POSTGRESQL DATABASE
-- ============================================

-- INSERT PHOTOS
INSERT INTO photos (url, caption, is_locked, created_at) VALUES
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447253/h9_dfyqbj.jpg', 'Khoảnh khắc dễ thương trong nắng sớm', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447253/h6_cqtydj.jpg', 'Một chút dịu dàng giữa không gian quen thuộc', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447252/h8_ptxsrg.jpg', 'Ánh mắt thật hiền – một khung hình rất đẹp', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447238/h3_oqujlt.jpg', 'Ngồi xuống một chút, để lưu lại một bức ảnh thật xinh', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447238/h10_l9a3bq.jpg', 'Nét cổ điển pha chút tinh tế, nổi bật giữa không gian', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447227/h2_lfdzuw.jpg', 'Tự nhiên và nhẹ nhàng – vẻ đẹp đúng khoảnh khắc', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447226/h7_tbphpf.jpg', 'Khung cảnh ấm áp và tràn đầy cảm xúc', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447220/h4_taycha.jpg', 'Một góc ảnh rất nghệ – cảm giác bình yên', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447215/h1_s6nvk4.jpg', 'Nụ cười thật tươi – khoảnh khắc đáng yêu', false, NOW()),
('https://res.cloudinary.com/dclomtdc3/image/upload/v1764447214/h5_ilklny.jpg', 'Bức ảnh ghi lại một buổi tối thật đẹp', false, NOW());

-- INSERT VIDEOS
INSERT INTO videos (title, url, type, description, is_locked, created_at) VALUES
('Khoảnh khắc đáng nhớ', 'https://res.cloudinary.com/dclomtdc3/video/upload/v1764446593/6578534275909_wwsuzb.mp4', 'video', 'Một đoạn video ngắn nhưng tràn đầy kỷ niệm đẹp.', false, NOW());

-- INSERT WISHES
INSERT INTO wishes (text, friend_name, nickname, is_viewed, created_at) VALUES
('Chúc em sinh nhật vui vẻ! Chúc em luôn xinh đẹp, hạnh phúc và gặp thật nhiều điều tốt đẹp trong cuộc sống ❤️', 'Triệu', 'Khỉ già', false, NOW());