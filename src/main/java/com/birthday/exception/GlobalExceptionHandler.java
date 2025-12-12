package com.birthday.exception;

import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, Object>> handleSQLException(SQLException e) {
        logger.error("SQL Exception: ", e);
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Database connection error");
        error.put("message", "Không thể kết nối đến database. Vui lòng thử lại sau.");
        error.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Map<String, Object>> handlePersistenceException(PersistenceException e) {
        logger.error("Persistence Exception: ", e);
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Database error");
        error.put("message", "Lỗi khi truy cập database. Có thể database chưa sẵn sàng.");
        error.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException e) {
        logger.error("Data Access Exception: ", e);
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Database access error");
        
        // Kiểm tra nếu là connection error
        if (e instanceof DataAccessResourceFailureException || 
            e.getCause() instanceof SQLTransientConnectionException ||
            (e.getMessage() != null && e.getMessage().contains("Connection"))) {
            error.put("message", "Không thể kết nối đến database. Database có thể chưa sẵn sàng hoặc bị suspend.");
            error.put("hint", "Vui lòng kiểm tra database status trên Render Dashboard và đảm bảo database đang 'Available'");
        } else {
            error.put("message", "Lỗi khi truy cập database: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
        }
        
        error.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        error.put("exceptionType", e.getClass().getSimpleName());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(SQLTransientConnectionException.class)
    public ResponseEntity<Map<String, Object>> handleSQLTransientConnectionException(SQLTransientConnectionException e) {
        logger.error("SQL Transient Connection Exception: ", e);
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Database connection timeout");
        error.put("message", "Kết nối database timeout. Database có thể chưa sẵn sàng hoặc bị suspend.");
        error.put("hint", "Vui lòng kiểm tra database status trên Render Dashboard. Free tier database có thể bị suspend sau khi không dùng.");
        error.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime Exception: ", e);
        
        // Kiểm tra nếu là lỗi database connection
        if (e.getMessage() != null && (
            e.getMessage().contains("Connection") ||
            e.getMessage().contains("database") ||
            e.getMessage().contains("SQL") ||
            e.getCause() instanceof SQLException
        )) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Database connection error");
            error.put("message", "Không thể kết nối đến database. Vui lòng thử lại sau.");
            error.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Internal server error");
        error.put("message", e.getMessage() != null ? e.getMessage() : "Đã xảy ra lỗi không xác định");
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(IOException e) {
        logger.error("IO Exception: ", e);
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "File processing error");
        error.put("message", "Lỗi khi xử lý file: " + e.getMessage());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        logger.error("Generic Exception: ", e);
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Internal server error");
        error.put("message", "Đã xảy ra lỗi không xác định");
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

