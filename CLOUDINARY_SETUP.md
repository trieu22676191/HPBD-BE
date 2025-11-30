# Hướng dẫn cấu hình Cloudinary

## Bước 1: Tạo tài khoản Cloudinary

1. Truy cập https://cloudinary.com/
2. Đăng ký tài khoản miễn phí
3. Sau khi đăng nhập, vào **Dashboard**

## Bước 2: Lấy thông tin API

Trong Dashboard, bạn sẽ thấy:

- **Cloud name**: Tên cloud của bạn
- **API Key**: Khóa API
- **API Secret**: Bí mật API

## Bước 3: Cấu hình trong application.properties

Mở file `HPBD-BE/src/main/resources/application.properties` và cập nhật:

```properties
# Cloudinary Configuration
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

Thay thế:

- `your_cloud_name` → Cloud name của bạn
- `your_api_key` → API Key của bạn
- `your_api_secret` → API Secret của bạn

## Bước 4: Khởi động lại Backend

Sau khi cấu hình xong, khởi động lại Spring Boot application:

```bash
cd HPBD-BE
mvn spring-boot:run
```

## Lưu ý

- **Bảo mật**: Không commit file `application.properties` có chứa API Secret lên Git
- **Giới hạn**: Tài khoản miễn phí có giới hạn:
  - 25GB storage
  - 25GB bandwidth/tháng
  - 25 triệu transformations/tháng
- **Folder**: Ảnh sẽ được lưu trong folder `hpbd-photos` trên Cloudinary

## Kiểm tra

Sau khi upload ảnh thành công, URL sẽ có dạng:

```
https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/hpbd-photos/{filename}.jpg
```
