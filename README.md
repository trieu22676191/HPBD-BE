# HPBD Backend - Spring Boot API

Backend API cho trang web chúc mừng sinh nhật sử dụng Spring Boot và Java.

## Công nghệ sử dụng

- **Spring Boot 3.2.0**
- **Java 17**
- **Spring Data JPA**
- **MariaDB** (Database)
- **Maven**

## Cấu trúc Project

```
HPBD-BE/
├── src/
│   ├── main/
│   │   ├── java/com/birthday/
│   │   │   ├── HPBDApplication.java
│   │   │   ├── controller/     # REST Controllers
│   │   │   ├── service/        # Business Logic
│   │   │   ├── repository/    # Data Access Layer
│   │   │   ├── entity/         # Database Entities
│   │   │   └── dto/            # Data Transfer Objects
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## API Endpoints

### Wishes (Lời Chúc)
- `GET /api/wishes` - Lấy tất cả lời chúc
- `POST /api/wishes` - Tạo lời chúc mới
- `PUT /api/wishes/{id}` - Cập nhật lời chúc
- `DELETE /api/wishes/{id}` - Xóa lời chúc

### Photos (Ảnh)
- `GET /api/photos` - Lấy tất cả ảnh
- `POST /api/photos` - Tạo ảnh mới
- `PUT /api/photos/{id}` - Cập nhật ảnh
- `DELETE /api/photos/{id}` - Xóa ảnh

### Videos (Video)
- `GET /api/videos` - Lấy tất cả video
- `POST /api/videos` - Tạo video mới
- `PUT /api/videos/{id}` - Cập nhật video
- `DELETE /api/videos/{id}` - Xóa video

## Cài đặt và chạy

### Yêu cầu
- Java 17 hoặc cao hơn
- Maven 3.6+

### Chạy ứng dụng

```bash
# Build project
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

## Cấu hình Database

### MariaDB Setup
1. Cài đặt MariaDB và tạo database:
```sql
CREATE DATABASE birthday_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Cập nhật thông tin kết nối trong `application-dev.properties` hoặc `application-prod.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/birthday_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Chạy ứng dụng:
```bash
# Development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Ví dụ Request/Response

### Tạo lời chúc
```json
POST /api/wishes
{
  "text": "Chúc mừng sinh nhật!",
  "friendName": "Nguyễn Văn A",
  "nickname": "A"
}
```

### Tạo ảnh
```json
POST /api/photos
{
  "url": "https://example.com/image.jpg",
  "caption": "Kỷ niệm đẹp"
}
```

### Tạo video
```json
POST /api/videos
{
  "title": "Kỷ niệm đẹp",
  "url": "https://www.youtube.com/embed/...",
  "type": "iframe",
  "description": "Những khoảnh khắc đáng nhớ"
}
```

