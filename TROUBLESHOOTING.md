# Troubleshooting Database Connection Issues

## Vấn đề: Database Connection Timeout

Khi gặp lỗi `Connection is not available, request timed out after 60000ms`, có thể do các nguyên nhân sau:

### 1. Database chưa sẵn sàng hoàn toàn
- Database mới tạo cần 5-10 phút để provisioning hoàn tất
- **Giải pháp**: Đợi thêm vài phút, sau đó test lại endpoint `/ping/db`

### 2. Database bị Suspend (Free Tier)
- Render Free Tier có thể suspend database sau khi không dùng
- **Giải pháp**: 
  1. Vào Render Dashboard → Database `birthday-db`
  2. Kiểm tra status: Nếu là "Suspended", click "Resume Database"
  3. Đợi 2-3 phút để database resume
  4. Test lại endpoint `/ping/db`

### 3. Database và Web Service khác Region
- Database và Web Service phải cùng region (Singapore)
- **Giải pháp**: Đảm bảo cả hai đều ở region "Singapore"

### 4. Database credentials không đúng
- **Giải pháp**: 
  1. Vào Render Dashboard → Database → Connections
  2. Copy External Database URL
  3. Kiểm tra trong Web Service → Environment Variables
  4. Đảm bảo `DATABASE_URL` đúng

## Các bước kiểm tra

### Bước 1: Test Database Connection
```bash
curl https://hpbd-be-7rmt.onrender.com/ping/db
```

**Kết quả mong đợi:**
- ✅ `{"status": "ok"}` → Database đã kết nối
- ❌ `{"status": "error"}` → Database chưa kết nối, cần xử lý

### Bước 2: Kiểm tra Database Status trên Render
1. Vào Render Dashboard → Database `birthday-db`
2. Kiểm tra:
   - Status phải là "Available" (không phải "Suspended" hoặc "Provisioning")
   - Region phải là "Singapore"
   - Credentials phải hiển thị (không phải "Unavailable until database is ready")

### Bước 3: Kiểm tra Web Service Environment Variables
1. Vào Render Dashboard → Web Service `hpbd-backend`
2. Vào tab "Environment"
3. Kiểm tra:
   - `DATABASE_URL` có giá trị
   - `DATABASE_USER` có giá trị
   - `DATABASE_PASSWORD` có giá trị
   - `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

### Bước 4: Restart Database (nếu cần)
1. Vào Database → Networking
2. Click "Restart Database"
3. Đợi 2-3 phút
4. Test lại

### Bước 5: Xóa và Tạo lại Database (nếu vẫn không được)
1. **Lưu ý**: Chỉ làm bước này nếu các bước trên không giải quyết được
2. Vào Database → Delete Database
3. Tạo lại database:
   - Name: `birthday-db`
   - Region: `Singapore`
   - Plan: `Free`
4. Đợi database provisioning (5-10 phút)
5. Redeploy Web Service để nhận biến môi trường mới

## Tạo Schema thủ công

Nếu database connection OK nhưng API vẫn không load được data, có thể schema chưa được tạo:

### Cách 1: Sử dụng psql
```bash
# Lấy External Database URL từ Render Dashboard
psql "postgresql://birthday_user:password@dpg-xxxxx-a.singapore-postgres.render.com:5432/birthday_db_1jko" -f create-schema.sql
```

### Cách 2: Sử dụng pgAdmin
1. Mở pgAdmin
2. Tạo connection mới với External Database URL
3. Chạy file `create-schema.sql`

### Cách 3: Để Hibernate tự động tạo
- Nếu `SPRING_JPA_HIBERNATE_DDL_AUTO=update` đã được set
- Hibernate sẽ tự động tạo schema khi database sẵn sàng
- Kiểm tra logs để xem có message "Database available, enabling schema management"

## Test API sau khi fix

```bash
# Test health check
curl https://hpbd-be-7rmt.onrender.com/ping

# Test database connection
curl https://hpbd-be-7rmt.onrender.com/ping/db

# Test API endpoints
curl https://hpbd-be-7rmt.onrender.com/api/wishes
curl https://hpbd-be-7rmt.onrender.com/api/photos
curl https://hpbd-be-7rmt.onrender.com/api/videos
```

## Logs để kiểm tra

Sau khi deploy, kiểm tra logs để tìm:
- ✅ `Database connection successful on attempt X`
- ✅ `Database available, enabling schema management`
- ✅ `HikariPool-1 - Start completed`
- ❌ `Database not available after X attempts`
- ❌ `Connection is not available, request timed out`

## Liên hệ hỗ trợ

Nếu vẫn gặp vấn đề sau khi thử tất cả các bước trên:
1. Kiểm tra logs chi tiết trên Render Dashboard
2. Kiểm tra Render Status: https://status.render.com
3. Contact Render Support nếu cần

