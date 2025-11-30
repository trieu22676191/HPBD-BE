-- ============================================
-- SỬA CỘT URL TRONG BẢNG VIDEOS VÀ PHOTOS
-- ============================================

USE birthday_db;

-- Sửa cột url trong bảng videos từ VARCHAR/TEXT sang LONGTEXT
-- để lưu được URL dài (như base64 video có thể rất dài)
ALTER TABLE videos 
MODIFY COLUMN url LONGTEXT NOT NULL COMMENT 'URL video hoặc embed URL hoặc base64';

-- Sửa cột url trong bảng photos từ TEXT sang LONGTEXT
-- để lưu được base64 ảnh lớn
ALTER TABLE photos 
MODIFY COLUMN url LONGTEXT NOT NULL COMMENT 'URL hoặc base64 của ảnh';

-- Thêm cột is_locked vào bảng photos (nếu chưa có)
ALTER TABLE photos 
ADD COLUMN IF NOT EXISTS is_locked BOOLEAN DEFAULT FALSE COMMENT 'Trạng thái khóa ảnh';

-- Thêm index cho is_locked
ALTER TABLE photos 
ADD INDEX IF NOT EXISTS idx_is_locked (is_locked);

-- Thêm cột is_locked vào bảng videos (nếu chưa có)
ALTER TABLE videos 
ADD COLUMN IF NOT EXISTS is_locked BOOLEAN DEFAULT FALSE COMMENT 'Trạng thái khóa video';

-- Thêm index cho is_locked trong videos
ALTER TABLE videos 
ADD INDEX IF NOT EXISTS idx_is_locked (is_locked);

-- Thêm cột is_viewed vào bảng wishes (nếu chưa có)
ALTER TABLE wishes 
ADD COLUMN IF NOT EXISTS is_viewed BOOLEAN DEFAULT FALSE COMMENT 'Trạng thái đã xem';

-- Thêm index cho is_viewed trong wishes
ALTER TABLE wishes 
ADD INDEX IF NOT EXISTS idx_is_viewed (is_viewed);

-- Kiểm tra lại cấu trúc bảng
DESCRIBE videos;
DESCRIBE photos;

