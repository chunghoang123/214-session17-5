## 2. Mô tả (Giải thích cách hoạt động)

**Cách đăng ký ErrorHandler vào RedisCacheManager:**
1. Tạo class cấu hình `CacheConfig` có gắn annotation `@Configuration` và `@EnableCaching`.
2. Cho class này implements interface `CachingConfigurer`.
3. Ghi đè (override) phương thức `errorHandler()` và trả về một instance của `CustomCacheErrorHandler` vừa tạo.

**Cơ chế hoạt động của Fallback & Silent Fail:**
Khi Redis bị sập (ví dụ: `docker stop redis-lab`), các annotation như `@Cacheable` sẽ cố gắng kết nối để lấy dữ liệu. Việc kết nối thất bại sẽ sinh ra một ngoại lệ (`RuntimeException`).
* Thay vì ném thẳng lỗi này ra ngoài làm ứng dụng crash (trả về lỗi HTTP 500), Spring Cache sẽ chặn nó lại và chuyển vào `CustomCacheErrorHandler`.
* Tại phương thức `handleCacheGetError`, lỗi chỉ được in ra console qua lệnh `log.warn` ("Redis connection error on GET, falling back to DB...").
* Vì ngoại lệ đã được xử lý êm đẹp (silent fail), Spring ngầm hiểu rằng không lấy được dữ liệu trong cache. Kết quả là hệ thống tự động fallback, tiếp tục đi vào hàm Service để thực thi câu lệnh SQL lấy dữ liệu từ Database và trả về HTTP 200 thành công cho người dùng mà không làm gián đoạn luồng xử lý.
# 214-session17-5
