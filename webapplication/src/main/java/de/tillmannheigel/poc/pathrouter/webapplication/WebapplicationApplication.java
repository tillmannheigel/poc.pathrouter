package de.tillmannheigel.poc.pathrouter.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class WebapplicationApplication {

    @GetMapping("/")
    public String getHome() {
        return "Moin";
    }



	public static void main(String[] args) {
		SpringApplication.run(WebapplicationApplication.class, args);
	}
}
