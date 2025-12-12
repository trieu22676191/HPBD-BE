package com.birthday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ping")
@CrossOrigin(origins = "*")
public class HealthController {
    
    @Autowired(required = false)
    private DataSource dataSource;
    
    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }
    
    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> checkDatabase() {
        Map<String, Object> response = new HashMap<>();
        
        if (dataSource == null) {
            response.put("status", "error");
            response.put("message", "DataSource not configured");
            return ResponseEntity.status(503).body(response);
        }
        
        try (Connection connection = dataSource.getConnection()) {
            boolean isValid = connection.isValid(10);
            if (isValid) {
                response.put("status", "ok");
                response.put("message", "Database connection successful");
                response.put("database", connection.getCatalog());
                response.put("url", connection.getMetaData().getURL().replaceAll(":[^:@]+@", ":****@"));
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Database connection invalid");
                return ResponseEntity.status(503).body(response);
            }
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Database connection failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(503).body(response);
        }
    }
}

