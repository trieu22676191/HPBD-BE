package com.birthday.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class JpaConfig {

    private static final Logger logger = LoggerFactory.getLogger(JpaConfig.class);

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(DataSource dataSource) {
        return hibernateProperties -> {
            // Kiểm tra xem database có sẵn sàng không
            boolean dbAvailable = checkDatabaseAvailability(dataSource);
            
            if (!dbAvailable) {
                logger.warn("Database not available, disabling schema management");
                // Tắt schema management khi DB chưa sẵn sàng
                hibernateProperties.put("hibernate.hbm2ddl.auto", "none");
                hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", false);
            } else {
                logger.info("Database available, enabling schema management");
                // Cho phép schema update khi DB sẵn sàng
                hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
            }
        };
    }

    private boolean checkDatabaseAvailability(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2); // Timeout 2 giây
        } catch (SQLException e) {
            logger.debug("Database not available: {}", e.getMessage());
            return false;
        }
    }
}

