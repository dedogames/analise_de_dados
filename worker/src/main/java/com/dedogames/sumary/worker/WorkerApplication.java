package com.dedogames.sumary.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.dedogames.summary.shared.entities")
@EnableJpaRepositories("com.dedogames.summary.shared.repositories")
public class WorkerApplication {
	public static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
	}

}


