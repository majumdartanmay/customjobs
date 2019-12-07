package com.customjobs.networking;

import com.customjobs.networking.configurations.FileStorageConfigurations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageConfigurations.class
})
public class CustomJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomJobsApplication.class, args);
	}

}
