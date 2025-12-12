package com.birthday.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

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
        // Đọc từ environment variable trực tiếp (ưu tiên hơn @Value)
        String envDatabaseUrl = System.getenv("DATABASE_URL");
        String url = (envDatabaseUrl != null && !envDatabaseUrl.isEmpty()) ? envDatabaseUrl : databaseUrl;

        logger.info("DATABASE_URL from System.getenv(): {}", envDatabaseUrl != null ? "***" : "null");
        logger.info("DATABASE_URL from @Value: {}", databaseUrl != null && !databaseUrl.isEmpty() ? "***" : "null");
        logger.info("Using DATABASE_URL: {}",
                url != null && !url.isEmpty() ? "***" : "null (will use application.properties)");

        String finalUrl;
        String finalUsername;
        String finalPassword;
        String finalDriver;

        // Nếu có DATABASE_URL từ environment, parse và dùng
        if (url != null && !url.isEmpty()) {
            // Nếu DATABASE_URL có dạng postgresql://... (từ Render), cần parse
            if (url.startsWith("postgresql://") || url.startsWith("postgres://")) {
                ParsedDbInfo parsed = parsePostgresUrl(url);
                finalUrl = parsed.url;
                finalUsername = parsed.username;
                finalPassword = parsed.password;
                finalDriver = "org.postgresql.Driver";
            } else if (url.startsWith("jdbc:")) {
                finalUrl = url;
                finalUsername = username;
                finalPassword = password;
                finalDriver = (dbDriver != null && !dbDriver.isEmpty()) ? dbDriver : "org.postgresql.Driver";
            } else {
                // Fallback về application.properties
                finalUrl = datasourceUrl;
                finalUsername = username;
                finalPassword = password;
                finalDriver = (dbDriver != null && !dbDriver.isEmpty()) ? dbDriver : "org.postgresql.Driver";
            }
        } else {
            // Nếu không có DATABASE_URL, tạo DataSource từ application.properties
            finalUrl = datasourceUrl;
            finalUsername = username;
            finalPassword = password;
            finalDriver = (dbDriver != null && !dbDriver.isEmpty()) ? dbDriver : "org.postgresql.Driver";
        }

        // Tạo HikariCP DataSource với cấu hình connection pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(finalUrl);
        if (finalUsername != null && !finalUsername.isEmpty()) {
            config.setUsername(finalUsername);
        }
        if (finalPassword != null && !finalPassword.isEmpty()) {
            config.setPassword(finalPassword);
        }
        config.setDriverClassName(finalDriver);
        
        // Connection pool settings
        config.setConnectionTimeout(30000);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(600000);
        config.setConnectionTestQuery("SELECT 1");
        config.setLeakDetectionThreshold(60000);
        
        logger.info("Creating DataSource with URL: {}", finalUrl.replaceAll(":[^:@]+@", ":****@"));
        
        return new HikariDataSource(config);
    }

    private static class ParsedDbInfo {
        String url;
        String username;
        String password;
    }

    private ParsedDbInfo parsePostgresUrl(String url) {
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

            ParsedDbInfo info = new ParsedDbInfo();
            info.url = dbUrl;
            info.username = dbUser;
            info.password = dbPassword;
            return info;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing DATABASE_URL: " + url, e);
        }
    }
}
