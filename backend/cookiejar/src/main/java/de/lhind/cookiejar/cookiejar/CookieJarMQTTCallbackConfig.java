package de.lhind.cookiejar.cookiejar;

import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CookieJarMQTTCallbackConfig {

	private String uri = "mqtt://vabeykru:A6symGuRC6Oo@m21.cloudmqtt.com:16150";
	
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
