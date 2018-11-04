package de.lhind.cookiejar.cookiejar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class CookiejarApplication {

	public static void main(String[] args) {
		// CookieJarMQTTCallbackImpl s = new
		// CookieJarMQTTCallbackImpl("mqtt://user:password@server:port");
		SpringApplication.run(CookiejarApplication.class, args);

	}
}
