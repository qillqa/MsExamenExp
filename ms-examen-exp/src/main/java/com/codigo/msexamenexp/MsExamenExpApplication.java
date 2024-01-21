package com.codigo.msexamenexp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients("com.codigo.msexamenexp")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class MsExamenExpApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsExamenExpApplication.class, args);
	}

}
