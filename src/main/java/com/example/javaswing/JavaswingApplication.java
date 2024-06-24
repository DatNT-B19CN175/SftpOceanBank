package com.example.javaswing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.swing.*;

@SpringBootApplication
public class JavaswingApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
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
