# Hướng dẫn Deploy với Docker trên Render

## Các file đã tạo

✅ `Dockerfile` - File build Docker image
✅ `.dockerignore` - Loại trừ các file không cần thiết khi build

## Cấu hình trên Render

### 1. Language
- Chọn: **Docker**

### 2. Root Directory
- Để trống (nếu code ở root của repository)
- Hoặc điền `HPBD-BE` (nếu dùng monorepo)

### 3. Build Command
- Để trống (Docker sẽ tự động build từ Dockerfile)

### 4. Start Command
- Để trống (Docker sẽ tự động chạy từ Dockerfile)

### 5. Environment Variables
Thêm các biến môi trường sau:

**Database:**
- `DATABASE_URL` - Từ PostgreSQL service (Render tự tạo)
- `DATABASE_USER` - Từ PostgreSQL service
- `DATABASE_PASSWORD` - Từ PostgreSQL service
- `DB_DRIVER` = `org.postgresql.Driver`
- `DB_DIALECT` = `org.hibernate.dialect.PostgreSQLDialect`

**Cloudinary:**
- `CLOUDINARY_CLOUD_NAME` = `dclomtdc3`
- `CLOUDINARY_API_KEY` = `697821664471255`
- `CLOUDINARY_API_SECRET` = `M8y-hV7I_KlARcgK3VuaITkl8-4`

**Khác:**
- `PORT` = `8080` (Render tự set, nhưng có thể set để chắc chắn)
- `ALLOWED_ORIGINS` = `*` (sau khi có URL frontend, cập nhật)

## Lưu ý

1. Dockerfile sử dụng multi-stage build để tối ưu kích thước image
2. Java version: 17 (Eclipse Temurin)
3. Port mặc định: 8080
4. Memory: 512MB max, 256MB min

## Kiểm tra

Sau khi deploy, kiểm tra:
- Logs trong Render dashboard
- API endpoint: `https://your-app.onrender.com/api/wishes`

