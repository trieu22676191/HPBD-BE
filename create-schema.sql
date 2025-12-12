-- ============================================
-- CREATE SCHEMA CHO POSTGRESQL DATABASE
-- ============================================

-- Tạo bảng wishes (Lời chúc)
CREATE TABLE IF NOT EXISTS wishes (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(500) NOT NULL,
    friend_name VARCHAR(100) NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    is_viewed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng photos (Ảnh)
CREATE TABLE IF NOT EXISTS photos (
    id BIGSERIAL PRIMARY KEY,
    url TEXT NOT NULL,
    caption VARCHAR(200) NOT NULL,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng videos (Video)
CREATE TABLE IF NOT EXISTS videos (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    url TEXT NOT NULL,
    type VARCHAR(50),
    description VARCHAR(500),
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo indexes để tối ưu performance
CREATE INDEX IF NOT EXISTS idx_wishes_created_at ON wishes(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_photos_created_at ON photos(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_videos_created_at ON videos(created_at DESC);

