package com.fta.stock.tracking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

@SpringBootApplication
//@EnableScheduling
@OpenAPIDefinition
@EnableAspectJAutoProxy
public class Application {

	public static void main(String[] args) {
		System.out.println("Setting thea timezone"+ TimeZone.getTimeZone("GMT+3:00").getID());
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+3:00"));
		SpringApplication.run(Application.class, args);
	}

}
