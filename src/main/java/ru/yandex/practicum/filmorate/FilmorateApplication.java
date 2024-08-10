package ru.yandex.practicum.filmorate;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FilmorateApplication {
	private static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmorateApplication.class);

	public static void main(String[] args) {

		log.setLevel(Level.DEBUG);

		SpringApplication.run(FilmorateApplication.class, args);

	}

}
