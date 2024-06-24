package com.example.javaswing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public JavaSwing javaSwing() {
        return new JavaSwing();
    }

    @Bean
    public SFTPService sftpService() {
        return new SFTPService();
    }
}
