# Hướng dẫn tạo lại Database trên Render

## Bước 1: Tạo Database mới

1. **Đăng nhập vào Render Dashboard**

   - Truy cập: https://dashboard.render.com
   - Đăng nhập vào tài khoản của bạn

2. **Tạo PostgreSQL Database**

   - Click nút **"+ New"** ở góc trên bên phải
   - Chọn **"PostgreSQL"** từ dropdown menu

3. **Điền thông tin Database**

   - **Name**: `birthday-db` (quan trọng: phải đúng tên này để `render.yaml` tự động kết nối)
   - **Database**: `birthday_db` (tên database bên trong PostgreSQL)
   - **User**: `birthday_user` (tên user để kết nối)
   - **Region**: **`Singapore`** (QUAN TRỌNG: phải cùng region với Web Service)
   - **PostgreSQL Version**: Chọn version mới nhất (18 hoặc cao hơn)
   - **Plan**: `Free` (hoặc chọn plan phù hợp)

4. **Click "Create Database"**

   - Render sẽ bắt đầu tạo database
   - Quá trình này thường mất **1-3 phút**

5. **Đợi Database sẵn sàng**
   - Status sẽ chuyển từ "Provisioning" → "Available"
   - Khi thấy status là **"Available"** với dấu tick xanh, database đã sẵn sàng

## Bước 2: Kiểm tra Database Connection

1. **Vào Database `birthday-db`**

   - Click vào database vừa tạo trong dashboard

2. **Kiểm tra Connections**

   - Vào tab **"Info"** hoặc **"Connections"**
   - Copy **"External Database URL"** để dùng sau này
   - Lưu ý: Hostname sẽ có dạng `dpg-xxxxx-a.singapore-postgres.render.com`

3. **Kiểm tra Credentials**
   - Vào tab **"Info"** → **"Credentials"**
   - Đảm bảo credentials đã hiển thị (không phải "Unavailable until database is ready")
   - Username: `birthday_user`
   - Password: (sẽ được hiển thị)

## Bước 3: Kết nối Database với Web Service

Database sẽ **TỰ ĐỘNG** được kết nối với Web Service thông qua `render.yaml`:

```yaml
envVars:
  - key: DATABASE_URL
    fromDatabase:
      name: birthday-db
      property: connectionString
  - key: DATABASE_USER
    fromDatabase:
      name: birthday-db
      property: user
  - key: DATABASE_PASSWORD
    fromDatabase:
      name: birthday-db
      property: password
```

**Nếu Web Service đã tồn tại:**

- Render sẽ tự động cập nhật environment variables
- Có thể cần **Redeploy** Web Service để nhận biến môi trường mới:
  - Vào Web Service `hpbd-backend`
  - Click **"Manual Deploy"** → **"Deploy latest commit"**

## Bước 4: Tạo Schema (Tables)

Có 2 cách để tạo schema:

### Cách 1: Tự động bằng Hibernate (Khuyến nghị)

1. **Kiểm tra Environment Variable**

   - Vào Web Service `hpbd-backend` → **"Environment"**
   - Đảm bảo có biến: `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
   - Nếu chưa có, thêm vào:
     - **Key**: `SPRING_JPA_HIBERNATE_DDL_AUTO`
     - **Value**: `update`

2. **Redeploy Web Service**

   - Click **"Manual Deploy"** → **"Deploy latest commit"**
   - Đợi deploy hoàn tất

3. **Kiểm tra Logs**
   - Vào Web Service → **"Logs"**
   - Tìm các dòng:
     - ✅ `Database connection successful on attempt X`
     - ✅ `Database available, enabling schema management`
     - ✅ `HikariPool-1 - Start completed`
   - Hibernate sẽ tự động tạo tables khi database sẵn sàng

### Cách 2: Tạo thủ công bằng SQL

1. **Lấy External Database URL**

   - Vào Database → **"Info"** → **"Connections"**
   - Copy **"External Database URL"**
   - Format: `postgresql://birthday_user:password@dpg-xxxxx-a.singapore-postgres.render.com:5432/birthday_db_xxxxx`

2. **Kết nối bằng psql**

   ```bash
   # Windows PowerShell
   $env:PGPASSWORD="your_password"
   psql -h dpg-xxxxx-a.singapore-postgres.render.com -p 5432 -U birthday_user -d birthday_db_xxxxx

   # Hoặc dùng full URL
   psql "postgresql://birthday_user:password@dpg-xxxxx-a.singapore-postgres.render.com:5432/birthday_db_xxxxx"
   ```

3. **Chạy file create-schema.sql**

   ```sql
   -- Copy nội dung từ file create-schema.sql và paste vào psql
   -- Hoặc dùng:
   \i /path/to/create-schema.sql
   ```

4. **Hoặc dùng pgAdmin**
   - Mở pgAdmin
   - Tạo connection mới với External Database URL
   - Mở Query Tool
   - Copy nội dung `create-schema.sql` và chạy

## Bước 5: Kiểm tra và Test

### 5.1. Test Database Connection

```bash
# Test endpoint
curl https://hpbd-be-7rmt.onrender.com/ping/db
```

**Kết quả mong đợi:**

```json
{
  "status": "ok",
  "message": "Database connection successful",
  "database": "birthday_db_xxxxx",
  "url": "jdbc:postgresql://dpg-xxxxx-a.singapore-postgres.render.com:5432/birthday_db_xxxxx"
}
```

### 5.2. Test API Endpoints

```bash
# Health check
curl https://hpbd-be-7rmt.onrender.com/ping
# Kết quả: "ok"

# Test API
curl https://hpbd-be-7rmt.onrender.com/api/wishes
curl https://hpbd-be-7rmt.onrender.com/api/photos
curl https://hpbd-be-7rmt.onrender.com/api/videos
```

**Kết quả mong đợi:**

- Trả về mảng rỗng `[]` nếu chưa có data (đây là bình thường)
- Trả về lỗi 500 nếu schema chưa được tạo
- Trả về data nếu đã có data

### 5.3. Kiểm tra Tables trong Database

```sql
-- Kết nối vào database
psql "postgresql://birthday_user:password@dpg-xxxxx-a.singapore-postgres.render.com:5432/birthday_db_xxxxx"

-- Xem tất cả tables
\dt

-- Kết quả mong đợi:
-- public | photos  | table | birthday_user
-- public | videos  | table | birthday_user
-- public | wishes  | table | birthday_user
```

## Bước 6: Insert Sample Data (Tùy chọn)

Nếu muốn có data mẫu để test:

1. **Kết nối database** (như bước 4.2)
2. **Chạy file insert-sample-data.sql**
   ```sql
   -- Copy nội dung từ file insert-sample-data.sql và chạy
   ```

## Troubleshooting

### Vấn đề 1: Database không tự động kết nối với Web Service

**Giải pháp:**

- Kiểm tra tên database phải đúng là `birthday-db`
- Redeploy Web Service để nhận biến môi trường mới
- Kiểm tra Web Service → Environment → `DATABASE_URL` có giá trị

### Vấn đề 2: Connection timeout

**Giải pháp:**

- Đợi thêm 5-10 phút để database hoàn toàn sẵn sàng
- Kiểm tra database status phải là "Available"
- Restart database nếu cần

### Vấn đề 3: Schema không được tạo tự động

**Giải pháp:**

- Kiểm tra `SPRING_JPA_HIBERNATE_DDL_AUTO=update` trong environment variables
- Kiểm tra logs để xem có lỗi gì không
- Tạo schema thủ công bằng SQL (Cách 2)

### Vấn đề 4: API trả về lỗi 500

**Giải pháp:**

- Kiểm tra schema đã được tạo chưa (bước 5.3)
- Kiểm tra logs của Web Service để xem lỗi cụ thể
- Test endpoint `/ping/db` để kiểm tra database connection

## Checklist

Sau khi hoàn thành, đảm bảo:

- [ ] Database `birthday-db` đã được tạo với status "Available"
- [ ] Database và Web Service cùng region (Singapore)
- [ ] Web Service có environment variable `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
- [ ] Endpoint `/ping/db` trả về `{"status": "ok"}`
- [ ] Tables `wishes`, `photos`, `videos` đã được tạo
- [ ] API endpoints `/api/wishes`, `/api/photos`, `/api/videos` hoạt động

## Lưu ý quan trọng

1. **Tên database phải đúng**: `birthday-db` (không phải `birthday_db`)
2. **Region phải đúng**: `Singapore` (phải cùng với Web Service)
3. **Đợi database sẵn sàng**: Status phải là "Available" trước khi test
4. **Redeploy Web Service**: Sau khi tạo database, nên redeploy Web Service để nhận biến môi trường mới
5. **Free Tier limitations**: Database Free tier có thể bị suspend sau khi không dùng, cần resume lại

## Liên hệ

Nếu gặp vấn đề, kiểm tra:

- File `TROUBLESHOOTING.md` để xem các giải pháp chi tiết
- Logs trên Render Dashboard
- Render Status: https://status.render.com
