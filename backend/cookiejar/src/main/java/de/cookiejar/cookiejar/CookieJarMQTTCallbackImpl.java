package de.cookiejar.cookiejar;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;

import de.cookiejar.cookiejar.model.CookieJarWeight;
import de.cookiejar.cookiejar.model.CookieJarWeightRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieJarMQTTCallbackImpl implements MqttCallback {

	@Autowired
	private CookieJarWeightRepository repository;

	private final int qos = 1;
	private String topic = "weight";
	private MqttClient client;

	public CookieJarMQTTCallbackImpl(String uri) throws URISyntaxException, MqttException {
		this(new URI(uri));
	}

	public CookieJarMQTTCallbackImpl(URI uri) throws MqttException {
		String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
		String[] auth = this.getAuth(uri);
		String username = auth[0];
		String password = auth[1];
		String clientId = "MQTT-Java-Example";
		if (!uri.getPath().isEmpty()) {
			this.topic = uri.getPath().substring(1);
		}

		MqttConnectOptions conOpt = new MqttConnectOptions();
		conOpt.setCleanSession(true);
		conOpt.setUserName(username);
		conOpt.setPassword(password.toCharArray());

		this.client = new MqttClient(host, clientId, new MemoryPersistence());
		this.client.setCallback(this);
		this.client.connect(conOpt);

		this.client.subscribe(this.topic, qos);
	}

	// like this? see https://www.cloudmqtt.com/docs-java.html
	private String[] getAuth(URI uri) {
		String a = uri.getAuthority();
		String[] first = a.split("@");
		return first[0].split(":");
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO integrate logger
		log.error("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	// public void sendMessage(String payload) throws MqttException {
	// MqttMessage message = new MqttMessage(payload.getBytes());
	// message.setQos(qos);
	// this.client.publish(this.topic, message); // Blocking publish
	// }

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String payload = new String(message.getPayload());
		log.info(String.format("[%s] %s", topic, payload));
		// save grams in db
		repository.save(new CookieJarWeight(Double.valueOf(payload)));
	}

}
