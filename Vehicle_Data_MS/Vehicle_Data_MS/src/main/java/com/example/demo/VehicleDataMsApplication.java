package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.vehicle_shared.Entity", "com.example.demo.Entity"})
@EnableJpaRepositories(basePackages = {"com.example.demo.Repository"})
public class VehicleDataMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleDataMsApplication.class, args);
	}

}
