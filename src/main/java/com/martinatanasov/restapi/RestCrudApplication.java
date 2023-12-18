package com.martinatanasov.restapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Simple Spring JPA Rest and Swagger Example",
				version = "1.0.0",
				description = "Spring JPA rest project with MySQL database and swagger-ui for testing purpose",
				//termsOfService = "http://",
				contact = @Contact(
						name = "Martin Atanasov"
						//email = .....
				)
//				,license = @License(
//					name = "no license",
//					url = "http://"
//				)
		)
)
public class RestCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestCrudApplication.class, args);
	}

	//Start swagger with this link http://localhost:8080/swagger

}
