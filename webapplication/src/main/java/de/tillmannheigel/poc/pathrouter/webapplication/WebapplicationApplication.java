package de.tillmannheigel.poc.pathrouter.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebapplicationApplication {

	@RequestMapping("/")
	public void getPage(){
		return "Moin";
	}

	public static void main(String[] args) {
		SpringApplication.run(WebapplicationApplication.class, args);
	}
}
