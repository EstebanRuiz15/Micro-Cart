package com.emazon.micro_cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.emazon.micro_cart.infraestructur.feign")
public class MicroCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroCartApplication.class, args);
	}

}
