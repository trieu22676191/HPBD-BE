# Hướng dẫn tạo Database trên Render

## Bước 1: Tạo Database trên Render Dashboard

1. Đăng nhập vào [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → Chọn **"PostgreSQL"**
3. Điền thông tin:
   - **Name**: `birthday-db`
   - **Database**: `birthday_db`
   - **User**: `birthday_user`
   - **Region**: `Singapore` (phải cùng region với Web Service)
   - **Plan**: `Free`
4. Click **"Create Database"**
5. Đợi database được tạo (thường mất 1-2 phút)

## Bước 2: Kết nối Database với Web Service

Database sẽ tự động được kết nối với Web Service thông qua `render.yaml`:

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

## Bước 3: Tạo Schema (Tables)

Có 2 cách:

### Cách 1: Tự động tạo bằng Hibernate (Khuyến nghị)

1. Vào Web Service `hpbd-backend` trên Render Dashboard
2. Vào tab **"Environment"**
3. Thêm hoặc sửa biến môi trường:
   - **Key**: `SPRING_JPA_HIBERNATE_DDL_AUTO`
   - **Value**: `update`
4. Hoặc sửa trong `render.yaml`:
   ```yaml
   - key: SPRING_JPA_HIBERNATE_DDL_AUTO
     value: update
   ```
5. Redeploy Web Service
6. Hibernate sẽ tự động tạo tables khi ứng dụng start

### Cách 2: Tạo thủ công bằng SQL

1. Vào Database `birthday-db` trên Render Dashboard
2. Click tab **"Connect"** → Copy **"Internal Database URL"**
3. Sử dụng psql hoặc pgAdmin để kết nối:
   ```bash
   psql "postgresql://user:password@host:port/birthday_db"
   ```
4. Chạy file `create-schema.sql`:
   ```sql
   \i create-schema.sql
   ```
   Hoặc copy nội dung file `create-schema.sql` và chạy trực tiếp trong psql

## Bước 4: Insert Sample Data (Tùy chọn)

Sau khi tạo schema, bạn có thể insert sample data:

1. Kết nối database như bước 3
2. Chạy file `insert-sample-data.sql`:
   ```sql
   \i insert-sample-data.sql
   ```

## Bước 5: Kiểm tra

1. Kiểm tra logs của Web Service để đảm bảo kết nối thành công
2. Test API:
   - `GET /ping` → Phải trả về `"ok"`
   - `GET /api/wishes` → Phải trả về danh sách (có thể rỗng)
   - `GET /api/photos` → Phải trả về danh sách (có thể rỗng)
   - `GET /api/videos` → Phải trả về danh sách (có thể rỗng)

## Lưu ý

- Database và Web Service phải cùng **region** (Singapore)
- Đảm bảo DATABASE_URL có đầy đủ domain suffix (ví dụ: `dpg-xxxxx-a.singapore-postgres.render.com`)
- Nếu dùng `ddl-auto=update`, Hibernate sẽ tự động tạo/update schema khi start
- Nếu dùng `ddl-auto=none`, phải tạo schema thủ công
