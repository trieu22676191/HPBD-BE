package com.birthday.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        // Nếu DATABASE_URL có dạng postgresql://... (từ Render), cần parse
        if (datasourceUrl != null && (datasourceUrl.startsWith("postgresql://") || datasourceUrl.startsWith("postgres://"))) {
            return parsePostgresUrl(datasourceUrl);
        }
        
        // Nếu không có URL hoặc URL rỗng, để Spring Boot tự xử lý với application.properties
        // Tạo DataSource từ application.properties
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        
        if (datasourceUrl != null && !datasourceUrl.isEmpty() && datasourceUrl.startsWith("jdbc:")) {
            builder.url(datasourceUrl);
        } else if (datasourceUrl == null || datasourceUrl.isEmpty()) {
            // Fallback về MariaDB local nếu không có URL
            builder.url("jdbc:mariadb://localhost:3306/birthday_db?useSSL=false&serverTimezone=UTC");
        } else {
            builder.url(datasourceUrl);
        }
        
        if (username != null && !username.isEmpty()) {
            builder.username(username);
        }
        if (password != null && !password.isEmpty()) {
            builder.password(password);
        }
        
        return builder.build();
    }

    private DataSource parsePostgresUrl(String url) {
        try {
            // Parse URL dạng: postgresql://user:pass@host:port/dbname
            URI dbUri = new URI(url);
            
            String dbUrl = String.format("jdbc:postgresql://%s:%d%s",
                    dbUri.getHost(),
                    dbUri.getPort() == -1 ? 5432 : dbUri.getPort(),
                    dbUri.getPath());
            
            String[] userInfo = dbUri.getUserInfo() != null ? dbUri.getUserInfo().split(":") : new String[0];
            String dbUser = userInfo.length > 0 ? userInfo[0] : "postgres";
            String dbPassword = userInfo.length > 1 ? userInfo[1] : "";
            
            return DataSourceBuilder.create()
                    .url(dbUrl)
                    .username(dbUser)
                    .password(dbPassword)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing DATABASE_URL: " + url, e);
        }
    }
}

