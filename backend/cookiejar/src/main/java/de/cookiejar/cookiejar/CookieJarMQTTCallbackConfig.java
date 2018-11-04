package de.cookiejar.cookiejar;

import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CookieJarMQTTCallbackConfig {

	@Value( "${mqtt.uri}")
	private String uri;
	
	@Bean
	public MqttCallback cookieJarCallback() {
		try {
			return new CookieJarMQTTCallbackImpl(uri);
		} catch (URISyntaxException | MqttException e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
