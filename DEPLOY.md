# Hướng dẫn Deploy Backend lên Render

## Các file đã được cập nhật

1. ✅ `application.properties` - Hỗ trợ biến môi trường và PostgreSQL
2. ✅ `pom.xml` - Thêm PostgreSQL dependency
3. ✅ `render.yaml` - File cấu hình cho Render
4. ✅ `DataSourceConfig.java` - Tự động parse DATABASE_URL từ Render

## Các bước deploy

### Bước 1: Đăng ký và tạo project trên Render

1. Truy cập https://render.com
2. Đăng nhập bằng GitHub
3. Click "New +" → "Blueprint"
4. Kết nối repository `DieuHien`
5. Render sẽ tự động detect file `render.yaml`

**Hoặc tạo thủ công:**

1. Click "New +" → "Web Service"
2. Kết nối GitHub repository `DieuHien`
3. Cấu hình:
   - **Name**: `hpbd-backend`
   - **Region**: `Singapore` (gần Việt Nam nhất)
   - **Branch**: `main` (hoặc branch của bạn)
   - **Root Directory**: `HPBD-BE`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/hpbd-backend-1.0.0.jar`

### Bước 2: Tạo PostgreSQL Database

1. Click "New +" → "PostgreSQL"
2. Cấu hình:
   - **Name**: `birthday-db`
   - **Database**: `birthday_db`
   - **User**: `birthday_user`
   - **Region**: `Singapore`
   - **Plan**: `Free`
3. Lưu thông tin kết nối

### Bước 3: Link Database với Web Service

1. Vào Web Service → Settings
2. Tìm "Connections"
3. Link với PostgreSQL database `birthday-db`
4. Render sẽ tự động tạo biến môi trường

### Bước 4: Cấu hình Environment Variables

Vào Web Service → Environment, kiểm tra các biến sau:

**Tự động từ Database:**
- `DATABASE_URL` - Render tự tạo
- `DATABASE_USER` - Render tự tạo  
- `DATABASE_PASSWORD` - Render tự tạo

**Cần thêm thủ công:**
- `DB_DRIVER` = `org.postgresql.Driver`
- `DB_DIALECT` = `org.hibernate.dialect.PostgreSQLDialect`
- `CLOUDINARY_CLOUD_NAME` = `dclomtdc3`
- `CLOUDINARY_API_KEY` = `697821664471255`
- `CLOUDINARY_API_SECRET` = `M8y-hV7I_KlARcgK3VuaITkl8-4`
- `ALLOWED_ORIGINS` = `*` (sau khi có URL frontend, cập nhật thành URL frontend)

### Bước 5: Deploy

1. Click "Manual Deploy" → "Deploy latest commit"
2. Hoặc push code lên GitHub, Render sẽ auto-deploy
3. Xem logs trong tab "Logs"
4. URL sẽ có dạng: `https://hpbd-backend.onrender.com`

### Bước 6: Cập nhật CORS sau khi có URL frontend

1. Vào Web Service → Environment
2. Cập nhật `ALLOWED_ORIGINS`:
   ```
   https://your-frontend.vercel.app,http://localhost:5173
   ```
3. Redeploy

## Lưu ý quan trọng

1. **PostgreSQL vs MariaDB**: 
   - Render free chỉ có PostgreSQL
   - Code đã được cập nhật để hỗ trợ cả hai
   - Local vẫn dùng MariaDB, production dùng PostgreSQL

2. **Free tier**:
   - Web service có thể sleep sau 15 phút không dùng
   - Database free có giới hạn

3. **Build time**:
   - Lần đầu build có thể mất 5-10 phút
   - Các lần sau nhanh hơn

4. **Logs**:
   - Xem logs trong Render dashboard để debug
   - Nếu có lỗi, kiểm tra DATABASE_URL và các biến môi trường

## Kiểm tra sau khi deploy

1. Test API: `https://hpbd-backend.onrender.com/api/wishes`
2. Kiểm tra logs trong Render dashboard
3. Cập nhật URL backend trong frontend (biến `VITE_API_BASE_URL`)

## Troubleshooting

### Lỗi kết nối database
- Kiểm tra DATABASE_URL có đúng format không
- Đảm bảo database đã được link với web service

### Lỗi build
- Kiểm tra Java version (cần Java 17)
- Kiểm tra Maven dependencies

### Lỗi CORS
- Cập nhật ALLOWED_ORIGINS với URL frontend chính xác

