package com.example.PortalAPIv3;

import com.example.PortalAPIv3.API.Service.APIService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PortalApIv3Application {

	@Bean
	public APIService apiService() {
		return new APIService();
	}

	public static void main(String[] args) {
		SpringApplication.run(PortalApIv3Application.class, args);
	}
}
