package br.com.dbc.vemser.cinedev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class CinedevApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinedevApplication.class, args);
	}

}
