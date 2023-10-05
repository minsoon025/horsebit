package com.a406.horsebit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HorsebitApplication {

	public static void main(String[] args) {
		SpringApplication.run(HorsebitApplication.class, args);
	}

}
