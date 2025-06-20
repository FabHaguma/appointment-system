package com.appointmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class AppointmentsystemApplication {

	public static void main(String[] args) {
		// Load .env file and set as system properties before Spring Boot starts
		Dotenv.configure().ignoreIfMissing().systemProperties().load();
		SpringApplication.run(AppointmentsystemApplication.class, args);
	}

}
