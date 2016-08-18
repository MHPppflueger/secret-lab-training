package com.example;

import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// From: https://www.youtube.com/watch?v=ZyK5QrKCbwM

// Environment: http://localhost:8081/env
// Health: http://localhost:8081/health
// Refresh: curl -d{} http://localhost:8081/refresh

@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServicesApplication {
	
	@Bean
	CommandLineRunner commandLineRunner(ReservationRepository reservationRepository) {
		return new CommandLineRunner() {

			@Override
			public void run(String... arg0) throws Exception {
				Stream.of("Tristan", "Therese", "Uwe", "Angela").forEach(n -> reservationRepository.save(new Reservation(n)));
			}
			
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ReservationServicesApplication.class, args);
	}
}

@RefreshScope
@RestController
class MessageRestController {
	
	@Value("${message}")
	private String message;
	
	@RequestMapping("/message")
	String message() {
		return message;
	}
	
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@RestResource(path="by-name")
	Collection<Reservation> findByReservationName(String reservationName);
	
}

@Entity
class Reservation {
	
	@Id @GeneratedValue
	private Long id;
	private String reservationName;
	
	public Reservation() {
	}
	
	public Reservation(String reservationName) {
		super();
		this.reservationName = reservationName;
	}

	public Long getId() {
		return id;
	}

	public String getReservationName() {
		return reservationName;
	}
	
	
}