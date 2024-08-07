package com.example.javaswing;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@SpringBootApplication
@EnableScheduling
@Log4j2
public class JavaswingApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			log.info("UI look and feel set to system default");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			//e.printStackTrace();
			log.error("Failed to set UI look and feel. {}", e.getMessage());
		}
		log.info("Starting JavaSwing application...");
		SpringApplication.run(JavaswingApplication.class, args);
//		JavaSwing javaSwing = new JavaSwing();
//		javaSwing.isVisible();
//		javax.swing.SwingUtilities.invokeLater(() -> new JavaSwing());
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			JavaSwing javaSwing = ctx.getBean(JavaSwing.class);
			javaSwing.setVisible(true);
		};
	}
}
