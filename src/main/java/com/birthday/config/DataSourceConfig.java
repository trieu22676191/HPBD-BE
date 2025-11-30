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

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${DATABASE_USER:${spring.datasource.username:}}")
    private String username;

    @Value("${DATABASE_PASSWORD:${spring.datasource.password:}}")
    private String password;

    @Value("${DB_DRIVER:}")
    private String dbDriver;

    @Bean
    @Primary
    public DataSource dataSource() {
        // Ưu tiên đọc từ DATABASE_URL (từ Render/cloud providers)
        String url = databaseUrl != null && !databaseUrl.isEmpty() ? databaseUrl : datasourceUrl;

        // Nếu không có URL nào, để Spring Boot tự xử lý với application.properties
        if (url == null || url.isEmpty()) {
            return null; // Spring Boot sẽ dùng auto-configuration
        }

        // Nếu DATABASE_URL có dạng postgresql://... (từ Render), cần parse
        if (url.startsWith("postgresql://") || url.startsWith("postgres://")) {
            return parsePostgresUrl(url);
        }

        // Nếu có URL dạng jdbc:...
        if (url.startsWith("jdbc:")) {
            DataSourceBuilder<?> builder = DataSourceBuilder.create();
            builder.url(url);

            if (username != null && !username.isEmpty()) {
                builder.username(username);
            }
            if (password != null && !password.isEmpty()) {
                builder.password(password);
            }
            if (dbDriver != null && !dbDriver.isEmpty()) {
                builder.driverClassName(dbDriver);
            }

            return builder.build();
        }

        // Nếu URL không hợp lệ, để Spring Boot tự xử lý
        return null;
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
