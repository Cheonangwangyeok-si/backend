package com.likelion.mindiary.global.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initialize() {
        String sql = "ALTER DATABASE likelion CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;";
        jdbcTemplate.execute(sql);
    }
}
