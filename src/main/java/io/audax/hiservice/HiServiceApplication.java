package io.audax.hiservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@RestController
	class HiController {

		private static final Logger LOGGER = LoggerFactory.getLogger(HiController.class);
		private final RestTemplate restTemplate;

		HiController(RestTemplate restTemplate) {
			this.restTemplate = restTemplate;
		}

		@GetMapping("/hi")
		public String hi() {
			LOGGER.info("--------Hi method started--------");
			LOGGER.error("--------Hi method started, missing id--------");
			ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("https://postman-echo.com/post",
					"Hi, Cloud!", String.class);
			return responseEntity.getBody();
		}

		@GetMapping("/exception")
		public String exception() {
			throw new IllegalArgumentException("This id is invalid");
		}

		@ExceptionHandler(value = { IllegalArgumentException.class })
		protected ResponseEntity<String> handleConflict(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
}
